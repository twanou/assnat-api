package net.daneau.assnat.loaders.interventions;

import net.daneau.assnat.scrappers.AssNatLogScraper;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterventionsLoaderTest {

    @Mock
    private InterventionLoader interventionLoaderNoMatchMock;
    @Mock
    private InterventionLoader interventionLoaderMatchMock;
    @Mock
    private AssNatLogScraper assNatLogScraperMock;
    private InterventionsLoader interventionsLoader;

    @BeforeEach
    void setup() {
        when(interventionLoaderMatchMock.getInterventionMatchers()).thenReturn(List.of("1", "2"));
        when(interventionLoaderNoMatchMock.getInterventionMatchers()).thenReturn(List.of("1", "2", "3", "4"));
        this.interventionsLoader = new InterventionsLoader(List.of(interventionLoaderMatchMock, interventionLoaderNoMatchMock), assNatLogScraperMock);
    }

    @Test
    void load() {
        ScrapedLogEntry scrapedLogEntry = ScrapedLogEntry.builder().relativeUrl("relativeUrl").build();
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("0")
                .children(List.of(ScrapedLogNode.builder()
                        .title("1")
                        .children(List.of(ScrapedLogNode.builder()
                                .title("2")
                                .build()))
                        .build()))
                .build();
        when(assNatLogScraperMock.scrape("relativeUrl")).thenReturn(scrapedLogNode);
        this.interventionsLoader.load(scrapedLogEntry);
        verify(interventionLoaderMatchMock).load(scrapedLogEntry, scrapedLogNode.getChildren().get(0).getChildren().get(0));
        verify(interventionLoaderNoMatchMock, never()).load(any(), any());
    }
}