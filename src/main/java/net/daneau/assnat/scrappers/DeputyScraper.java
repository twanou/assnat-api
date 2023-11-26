package net.daneau.assnat.scrappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.daneau.assnat.scrappers.configuration.AssNatWebClient;
import net.daneau.assnat.scrappers.exceptions.ScrapingException;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.html.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeputyScraper {

    @Value("${scraper.assnat.deputies}")
    private final String relativeUrl;
    private final AssNatWebClient webClient;
    private final ErrorHandler errorHandler;

    private static final String INDEPENDENT = "Indépendant";
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
            HtmlPage deputyPage = this.getDeputyPage(cells.get(0));
            List<String> functions = this.getFunctions(deputyPage);
            deputies.add(
                    ScrapedDeputy.builder()
                            .title(this.getTitle(functions))
                            .firstName(StringUtils.strip(cells.get(0).getVisibleText().split(",")[1]))
                            .lastName(StringUtils.strip(cells.get(0).getVisibleText().split(",")[0]))
                            .district(cells.get(1).getVisibleText())
                            .party(this.independenceCheck(cells.get(2).getVisibleText()))
                            .functions(functions)
                            .image(this.getImage(deputyPage))
                            .build()
            );
        }
        log.info("Fin scraping députés");
        this.errorHandler.assertSize(rows.size(), deputies, () -> new ScrapingException("Nombre de député invalide"));
        deputies.sort(Comparator.comparing(deputy -> Normalizer.normalize(deputy.getLastName() + deputy.getFirstName(), Normalizer.Form.NFD)));
        return Collections.unmodifiableList(deputies);
    }

    private String independenceCheck(String party) {
        return StringUtils.containsIgnoreCase(party, INDEPENDENT) ? INDEPENDENT_PARTY : party;
    }

    private HtmlPage getDeputyPage(HtmlTableDataCell cell) {
        HtmlAnchor anchor = cell.getFirstByXPath(".//a");
        return this.webClient.getRelativePage(anchor.getHrefAttribute());
    }

    private String getTitle(List<String> functions) {
        return functions.stream()
                .anyMatch(function -> function.contains(DEPUTEE)) ? MME : M;
    }

    private HtmlImage getImage(HtmlPage deputyPage) {
        return deputyPage.getFirstByXPath("//img[@class='photoDepute']");
    }

    private List<String> getFunctions(HtmlPage deputyPage) {
        List<HtmlListItem> functionItmes = deputyPage.getByXPath("//div[@class='enteteFicheDepute']//ul//li");
        return functionItmes.stream()
                .map(DomNode::getVisibleText)
                .toList();
    }
}
