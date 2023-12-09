package net.daneau.assnat.scrapers;

import com.fasterxml.jackson.core.type.TypeReference;
import net.daneau.assnat.scrapers.configuration.AssNatWebClient;
import net.daneau.assnat.scrapers.models.ScrapedLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AssNatLogEntryScraperTest {

    @Spy
    private AssNatWebClient assNatWebClientSpy = new AssNatWebClient("file://");
    private AssNatLogEntryScraper assNatLogEntryScraper;
    private static final String LOG_ENTRY_PAGE = new File("src/test/resources/scrapers/pages/logentries.html").getAbsolutePath();
    private static final String JSON_EXPECTED_RESULTS = "src/test/resources/scrapers/results/logentries.json";

    @BeforeEach
    void setup() {
        this.assNatLogEntryScraper = new AssNatLogEntryScraper(LOG_ENTRY_PAGE, assNatWebClientSpy);
    }

    @Test
    void scrape() throws IOException {
        List<ScrapedLogEntry> scrapedLogEntries = this.assNatLogEntryScraper.scrape();
        verify(assNatWebClientSpy).getRelativePage(LOG_ENTRY_PAGE);
        TestUtils.assertFileEquals(JSON_EXPECTED_RESULTS, scrapedLogEntries, new TypeReference<>() {
        });
    }
}