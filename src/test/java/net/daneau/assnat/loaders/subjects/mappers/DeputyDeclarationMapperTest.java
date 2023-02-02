package net.daneau.assnat.loaders.subjects.mappers;

import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.Intervention;
import net.daneau.assnat.client.documents.subdocuments.SubjectData;
import net.daneau.assnat.client.documents.subdocuments.SubjectDataType;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyDeclarationMapperTest {
    
    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private DeputyDeclarationMapper deputyDeclarationMapper;

    @Test
    void map() {
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
        SubjectData expectedResult = SubjectData.builder()
                .type(SubjectDataType.DEPUTY_DECLARATION)
                .title(scrapedLogNode.getChildren().get(0).getTitle())
                .interventions(List.of(
                        Intervention.builder()
                                .deputyId(assignment.getDeputyId())
                                .partyId(assignment.getPartyId())
                                .ridingId(assignment.getRidingId())
                                .paragraphs(List.of("Bonjour", "oui"))
                                .build()))
                .build();
        when(deputyFinderMock.findByCompleteName("M. Lucien Bouchard")).thenReturn(assignment);
        List<SubjectData> subjects = this.deputyDeclarationMapper.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.get(0));
    }
}