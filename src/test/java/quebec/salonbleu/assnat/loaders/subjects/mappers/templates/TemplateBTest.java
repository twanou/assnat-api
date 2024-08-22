package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;
import test.utils.TestUUID;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateBTest {

    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private TemplateBImpl templateBImpl;

    @Test
    void map() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Dépôt de pétitions")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Super cause noble")
                        .anchor("#anchor")
                        .paragraphs(List.of("M. Drainville : Bonjour", "• (10 h 20) •", "Je dépose cette pétition"))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.PETITION)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Je dépose cette pétition"))
                                .build()))
                .build();
        when(deputyFinderMock.findByLastName("M. Drainville")).thenReturn(Optional.of(assignment));
        
        List<SubjectDetails> subjects = this.templateBImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    private static class TemplateBImpl extends TemplateB {

        public TemplateBImpl(DeputyFinder deputyFinder) {
            super(deputyFinder);
        }

        @Override
        public List<String> supports() {
            return null;
        }

        @Override
        protected List<String> format(List<String> paragraphs) {
            return paragraphs;
        }

        @Override
        protected SubjectType getSubjectType() {
            return SubjectType.PETITION;
        }
    }
}