package net.daneau.assnat.loaders.interventions;

import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.Intervention;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import net.daneau.assnat.client.repositories.SubjectRepository;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyDeclarationLoaderTest {

    @Mock
    private SubjectRepository subjectRepositoryMock;
    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private DeputyDeclarationLoader deputyDeclarationLoader;

    @Test
    void load() {
        ScrapedLogEntry scrapedLogEntry = ScrapedLogEntry.builder().session(1).legislature(2).date(LocalDate.now()).build();
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Déclarations des députés")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Souligner quelque chose d'important")
                        .children(List.of(ScrapedLogNode.builder()
                                .title("M. Lucien Bouchard")
                                .paragraphs(List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose"))
                                .build()))
                        .build()))
                .build();


        Assignment assignment = Assignment.builder().deputyId("1").partyId("2").ridingId("3").build();
        Subject expectedResult = Subject.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title(scrapedLogNode.getChildren().get(0).getTitle())
                .interventions(List.of(
                        Intervention.builder()
                                .deputyId(assignment.getDeputyId())
                                .partyId(assignment.getPartyId())
                                .ridingId(assignment.getRidingId())
                                .paragraphs(List.of("Bonjour", "oui"))
                                .build()))
                .date(LocalDate.now())
                .legislature(scrapedLogEntry.getLegislature())
                .session(scrapedLogEntry.getSession())
                .build();
        when(deputyFinderMock.findByCompleteName("M. Lucien Bouchard")).thenReturn(assignment);
        this.deputyDeclarationLoader.load(scrapedLogEntry, scrapedLogNode);
        verify(subjectRepositoryMock).save(expectedResult);
    }
}