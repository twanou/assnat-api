package quebec.salonbleu.assnat.loaders.subjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA;
import quebec.salonbleu.assnat.scrapers.AssNatLogScraper;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectLoaderTest {

    @Mock
    private TemplateA subjectDocumentTypeMapperNoMatchMock;
    @Mock
    private TemplateA subjectDocumentTypeMapperMatchMock;
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
                .pageId("123456")
                .legislature(1)
                .session(2)
                .date(LocalDate.of(2023, 1, 1))
                .build();
        when(subjectDocumentTypeMapperMatchMock.map(scrapedLogNode.getChildren().getFirst().getChildren().getFirst())).thenReturn(List.of(expectedSubject.getSubjectDetails()));
        when(assNatLogScraperMock.scrape("/relativeUrl/123456.html")).thenReturn(scrapedLogNode);

        this.subjectLoader.load("/relativeUrl/123456.html", expectedSubject.getDate(), expectedSubject.getLegislature(), expectedSubject.getSession());
        verify(subjectDocumentTypeMapperNoMatchMock, never()).map(any());
        verify(subjectRepositoryMock).saveAll(List.of(expectedSubject));
    }
}
