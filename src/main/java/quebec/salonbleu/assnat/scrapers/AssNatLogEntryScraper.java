package quebec.salonbleu.assnat.scrapers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTableDataCell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.scrapers.configuration.AssNatWebClient;
import quebec.salonbleu.assnat.scrapers.models.LogType;
import quebec.salonbleu.assnat.scrapers.models.LogVersion;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogEntry;
import quebec.salonbleu.assnat.scrapers.utils.ScrapeUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
            HtmlAnchor dateAnchor = cells.getFirst().getFirstByXPath(".//a");
            LocalDate date = LocalDate.parse(dateAnchor.getVisibleText());
            String[] session = cells.get(2).getVisibleText().split(",");
            logEntries.add(
                    ScrapedLogEntry.builder()
                            .date(date)
                            .note(StringUtils.trimToNull(Strings.CS.remove(cells.getFirst().getVisibleText(), date.toString())))
                            .relativeUrl(dateAnchor.getHrefAttribute())
                            .type(LogType.fromName(cells.get(1).getVisibleText().split(" ")[0]))
                            .legislature(ScrapeUtils.onlyDigitsToInt(session[0]))
                            .session(ScrapeUtils.onlyDigitsToInt(session[1]))
                            .version(LogVersion.fromText(cells.get(3).getVisibleText()))
                            .build()
            );
        }
        return Collections.unmodifiableList(logEntries);
    }
}