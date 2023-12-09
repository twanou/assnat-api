package net.daneau.assnat.loaders.subjects;

import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.repositories.SubjectRepository;
import net.daneau.assnat.loaders.subjects.mappers.SubjectDocumentTypeMapper;
import net.daneau.assnat.scrapers.AssNatLogScraper;
import net.daneau.assnat.scrapers.models.ScrapedLogNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectLoaderTest {

    @Mock
    private SubjectDocumentTypeMapper subjectDocumentTypeMapperNoMatchMock;
    @Mock
    private SubjectDocumentTypeMapper subjectDocumentTypeMapperMatchMock;
    @Mock
    private SubjectRepository subjectRepositoryMock;
    @Mock
    private AssNatLogScraper assNatLogScraperMock;
    private SubjectLoader subjectLoader;

    @BeforeEach
    void setup() {
        when(subjectDocumentTypeMapperMatchMock.supports()).thenReturn(List.of("1", "2"));
        when(subjectDocumentTypeMapperNoMatchMock.supports()).thenReturn(List.of("1", "2", "3", "4"));
        this.subjectLoader = new SubjectLoader(List.of(subjectDocumentTypeMapperMatchMock, subjectDocumentTypeMapperNoMatchMock), subjectRepositoryMock, assNatLogScraperMock);
    }

    @Test
    void load() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("0")
                .children(List.of(ScrapedLogNode.builder()
                        .title("1")
                        .children(List.of(ScrapedLogNode.builder()
                                .title("2")
                                .build()))
                        .build()))
                .build();
        Subject expectedSubject = Subject.builder()
                .subjectDetails(SubjectDetails.builder().title("titre").build())
                .legislature(1)
                .session(2)
                .date(LocalDate.of(2023, 1, 1))
                .build();
        when(subjectDocumentTypeMapperMatchMock.map(scrapedLogNode.getChildren().get(0).getChildren().get(0))).thenReturn(List.of(expectedSubject.getSubjectDetails()));
        when(assNatLogScraperMock.scrape("relativeUrl")).thenReturn(scrapedLogNode);
        this.subjectLoader.load("relativeUrl", expectedSubject.getDate(), expectedSubject.getLegislature(), expectedSubject.getSession());
        verify(subjectDocumentTypeMapperNoMatchMock, never()).map(any());
        verify(subjectRepositoryMock).saveAll(List.of(expectedSubject));
    }
}