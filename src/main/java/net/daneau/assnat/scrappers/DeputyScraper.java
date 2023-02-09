package net.daneau.assnat.scrappers;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import lombok.RequiredArgsConstructor;
import net.daneau.assnat.scrappers.configuration.AssNatWebClient;
import net.daneau.assnat.scrappers.exceptions.ScrapingException;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeputyScraper {

    @Value("${scraper.assnat.deputies}")
    private final String relativeUrl;
    private final AssNatWebClient webClient;
    private final ErrorHandler errorHandler;

    private static final String INDEPENDENT = "Indépendant";
    private static final String INDEPENDENT_PARTY = "Indépendant(e)";

    public List<ScrapedDeputy> scrape() {
        HtmlPage page = this.webClient.getRelativePage(relativeUrl);
        List<DomElement> rows = page.getByXPath("//table[@id='ListeDeputes']//tbody//tr");
        List<ScrapedDeputy> deputies = new ArrayList<>();
        for (DomElement row : rows) {
            List<HtmlTableDataCell> cells = row.getByXPath(".//td");
            deputies.add(
                    ScrapedDeputy.builder()
                            .firstName(StringUtils.strip(cells.get(0).getVisibleText().split(",")[1]))
                            .lastName(StringUtils.strip(cells.get(0).getVisibleText().split(",")[0]))
                            .district(cells.get(1).getVisibleText())
                            .party(this.independenceCheck(cells.get(2).getVisibleText()))
                            .build()
            );
        }
        this.errorHandler.assertSize(rows.size(), deputies, () -> new ScrapingException("Nombre de député invalide"));
        deputies.sort(Comparator.comparing(deputy -> Normalizer.normalize(deputy.getLastName() + deputy.getFirstName(), Normalizer.Form.NFD)));
        return Collections.unmodifiableList(deputies);
    }

    private String independenceCheck(String party) {
        return StringUtils.containsIgnoreCase(party, INDEPENDENT) ? INDEPENDENT_PARTY : party;
    }
}
