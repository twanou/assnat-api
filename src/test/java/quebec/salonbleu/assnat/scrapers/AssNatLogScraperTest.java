package quebec.salonbleu.assnat.scrapers;

import com.fasterxml.jackson.core.type.TypeReference;
import quebec.salonbleu.assnat.scrapers.configuration.AssNatWebClient;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.utils.TestUtils;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AssNatLogScraperTest {

    @Spy
    private AssNatWebClient assNatWebClientSpy = new AssNatWebClient("file://");
    private AssNatLogScraper assNatLogScraper;
    private static final String LOG_PAGE = new File("src/test/resources/scrapers/pages/log.html").getAbsolutePath();
    private static final String JSON_EXPECTED_RESULTS = "src/test/resources/scrapers/results/log.json";

    @BeforeEach
    void setup() {
        this.assNatLogScraper = new AssNatLogScraper(assNatWebClientSpy);
    }

    @Test
    void scrape() throws IOException {
        ScrapedLogNode scrapedLog = this.assNatLogScraper.scrape(LOG_PAGE);
        verify(assNatWebClientSpy).getRelativePage(LOG_PAGE);
        TestUtils.assertFileEquals(JSON_EXPECTED_RESULTS, scrapedLog, new TypeReference<>() {
        });
    }
}