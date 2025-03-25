package quebec.salonbleu.assnat.scrapers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlListItem;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTableDataCell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.scrapers.configuration.AssNatWebClient;
import quebec.salonbleu.assnat.scrapers.exceptions.ScrapingException;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeputyScraper {

    @Value("${scraper.assnat.deputies}")
    private final String relativeUrl;
    private final AssNatWebClient webClient;
    private final ErrorHandler errorHandler;

    private static final String INDEPENDENT = "Indépendant";
    private static final String INDEPENDENT_ACRONYM = "Ind.";
    private static final String INDEPENDENT_PARTY = "Indépendant(e)";
    private static final String DEPUTEE = "Députée";
    private static final String M = "M.";
    private static final String MME = "Mme";

    public List<ScrapedDeputy> scrape() {
        log.info("Début scraping députés");
        HtmlPage page = this.webClient.getRelativePage(relativeUrl);
        List<DomElement> rows = page.getByXPath("//table[@id='ListeDeputes']//tbody//tr");
        List<ScrapedDeputy> deputies = new ArrayList<>();
        for (DomElement row : rows) {
            List<HtmlTableDataCell> cells = row.getByXPath(".//td");
            HtmlPage deputyPage = this.getDeputyPage(cells.getFirst());
            List<String> functions = this.getFunctions(deputyPage);
            String party = cells.get(2).getVisibleText();
            deputies.add(
                    ScrapedDeputy.builder()
                            .title(this.getTitle(functions))
                            .firstName(StringUtils.strip(cells.getFirst().getVisibleText().split(",")[1]))
                            .lastName(StringUtils.strip(cells.getFirst().getVisibleText().split(",")[0]))
                            .district(StringUtils.strip(cells.get(1).getVisibleText()))
                            .party(this.independenceCheck(party) ? INDEPENDENT_PARTY : StringUtils.strip(party))
                            .partyAcronym(this.independenceCheck(party) ? INDEPENDENT_ACRONYM : this.getPartyAcronym(party))
                            .functions(functions)
                            .photo(this.getPhotoBase64(deputyPage))
                            .build()
            );
        }
        log.info("Fin scraping députés");
        this.errorHandler.assertSize(rows.size(), deputies, () -> new ScrapingException("Nombre de député invalide"));
        deputies.sort(Comparator.comparing(deputy -> StringUtils.stripAccents(deputy.getLastName() + deputy.getFirstName())));
        return Collections.unmodifiableList(deputies);
    }

    private boolean independenceCheck(String party) {
        return StringUtils.containsIgnoreCase(party, INDEPENDENT);
    }

    private String getPartyAcronym(String party) {
        String acronym = Arrays.stream(party.split(" "))
                .filter(s -> s.length() > 3)
                .map(s -> s.substring(0, 1))
                .collect(Collectors.joining());
        return StringUtils.upperCase(acronym);
    }

    private HtmlPage getDeputyPage(HtmlTableDataCell cell) {
        HtmlAnchor anchor = cell.getFirstByXPath(".//a");
        return this.webClient.getRelativePage(anchor.getHrefAttribute());
    }

    private String getTitle(List<String> functions) {
        return functions.stream()
                .anyMatch(function -> function.contains(DEPUTEE)) ? MME : M;
    }

    private List<String> getFunctions(HtmlPage deputyPage) {
        List<HtmlListItem> functionItmes = deputyPage.getByXPath("//div[@class='enteteFicheDepute']//ul//li");
        return functionItmes.stream()
                .map(DomNode::getVisibleText)
                .toList();
    }

    private String getPhotoBase64(HtmlPage deputyPage) {
        HtmlImage htmlImage = deputyPage.getFirstByXPath("//img[@class='photoDepute']");
        if (htmlImage != null) { // Les nouveaux députés n'ont pas toujours une photo...
            try {
                InputStream deputyImageStream = htmlImage.getWebResponse(true).getContentAsStream();
                return Base64.getEncoder().encodeToString(IOUtils.toByteArray(deputyImageStream));
            } catch (Exception e) {
                throw new ScrapingException("Impossible d'extraire la photo", e);
            }
        }
        return "";
    }
}
