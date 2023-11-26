package net.daneau.assnat.scrappers;

import com.fasterxml.jackson.core.type.TypeReference;
import net.daneau.assnat.scrappers.configuration.AssNatWebClient;
import net.daneau.assnat.scrappers.exceptions.ScrapingException;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeputyScraperTest {

    @Mock
    private ErrorHandler errorHandlerMock;
    @Spy
    private AssNatWebClient assNatWebClientSpy = new AssNatWebClient("file://");
    private DeputyScraper deputyScraper;
    private static final String DEPUTIES_PAGE = "src/test/resources/scrapers/pages/deputies.html";
    private static final String JSON_EXPECTED_RESULTS = "src/test/resources/scrapers/results/deputies.json";

    @BeforeEach
    void setup() {
        this.deputyScraper = new DeputyScraper(DEPUTIES_PAGE, assNatWebClientSpy, errorHandlerMock);
    }

    @Test
    void scrape() throws IOException {
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            args[0] = new File((String) args[0]).getAbsolutePath();
            return invocation.callRealMethod();
        }).when(assNatWebClientSpy).getRelativePage(anyString());

        List<ScrapedDeputy> scrapedDeputies = this.deputyScraper.scrape();

        verify(errorHandlerMock).assertSize(eq(scrapedDeputies.size()), eq(scrapedDeputies), ArgumentMatchers.<Supplier<ScrapingException>>any());
        TestUtils.assertFileEquals(JSON_EXPECTED_RESULTS, scrapedDeputies, new TypeReference<>() {
        });
    }
}