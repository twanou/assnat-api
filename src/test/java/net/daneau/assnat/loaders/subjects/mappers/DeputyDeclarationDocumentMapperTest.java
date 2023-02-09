package net.daneau.assnat.loaders.subjects.mappers;

import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
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
class DeputyDeclarationDocumentMapperTest {

    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private DeputyDeclarationDocumentMapper deputyDeclarationDocumentMapper;

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


        Assignment assignment = Assignment.builder().deputyId("1").partyId("2").districtId("3").build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title(scrapedLogNode.getChildren().get(0).getTitle())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .deputyId(assignment.getDeputyId())
                                .partyId(assignment.getPartyId())
                                .districtId(assignment.getDistrictId())
                                .paragraphs(List.of("Bonjour", "oui"))
                                .build()))
                .build();
        when(deputyFinderMock.findByCompleteName("M. Lucien Bouchard")).thenReturn(assignment);
        List<SubjectDetails> subjects = this.deputyDeclarationDocumentMapper.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.get(0));
    }
}