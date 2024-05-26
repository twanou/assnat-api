package net.daneau.assnat.scrappers.configuration;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssNatWebClientTest {

    @Mock
    private WebClient webClientMock;
    private AssNatWebClient assNatWebClient;

    @BeforeEach
    void setup() {
        this.assNatWebClient = new AssNatWebClient("baseUrl");
        ReflectionTestUtils.setField(this.assNatWebClient, "webClient", webClientMock);
    }

    @Test
    void getRelativePage() throws IOException {
        HtmlPage htmlPage = mock(HtmlPage.class);
        when(this.webClientMock.getPage("baseUrl/relativeUrl")).thenReturn(htmlPage);
        HtmlPage response = this.assNatWebClient.getRelativePage("/relativeUrl");
        assertSame(htmlPage, response);
    }
}