package net.daneau.assnat.scrappers;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import lombok.RequiredArgsConstructor;
import net.daneau.assnat.scrappers.configuration.AssNatWebClient;
import net.daneau.assnat.scrappers.models.LogType;
import net.daneau.assnat.scrappers.models.LogVersion;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.utils.ScrapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssNatLogEntryScraper {

    @Value("${scraper.assnat.logs}")
    private final String relativeUrl;
    private final AssNatWebClient webClient;

    public List<ScrapedLogEntry> scrape() {
        HtmlPage page = this.webClient.getRelativePage(relativeUrl);
        List<DomElement> rows = page.getByXPath("//table[@id='tblListeJournal']//tbody//tr");
        List<ScrapedLogEntry> logEntries = new ArrayList<>();
        for (DomElement row : rows) {
            List<HtmlTableDataCell> cells = row.getByXPath(".//td");
            HtmlAnchor anchor = (HtmlAnchor) cells.get(0).getByXPath(".//a").get(0);
            String[] session = cells.get(2).getVisibleText().split(",");
            logEntries.add(
                    ScrapedLogEntry.builder()
                            .date(LocalDate.parse(cells.get(0).getVisibleText()))
                            .relativeUrl(anchor.getHrefAttribute())
                            .type(LogType.fromName(cells.get(1).getVisibleText().split(" ")[0]))
                            .legislature(ScrapeUtils.onlyDigitsToInt(session[0]))
                            .session(ScrapeUtils.onlyDigitsToInt(session[1]))
                            .version(LogVersion.fromText(cells.get(3).getVisibleText()))
                            .build()
            );
        }
        return logEntries;
    }
}