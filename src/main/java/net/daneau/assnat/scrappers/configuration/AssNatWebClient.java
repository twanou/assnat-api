package net.daneau.assnat.scrappers.configuration;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import net.daneau.assnat.scrappers.exceptions.ScrapingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AssNatWebClient {

    private final String baseUrl;
    private final WebClient webClient;

    public AssNatWebClient(@Value("${scraper.assnat.baseurl}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = new WebClient();
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.webClient.getOptions().setDownloadImages(false);
    }

    public <P extends Page> P getRelativePage(String relativeUrl) {
        try {
            return this.webClient.getPage(baseUrl + relativeUrl);
        } catch (IOException e) {
            throw new ScrapingException("Impossible d'obtenir la page demandée", e);
        }
    }
}
