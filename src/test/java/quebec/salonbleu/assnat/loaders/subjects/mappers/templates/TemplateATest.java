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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateATest {

    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private TemplateAImpl templateAImpl;

    @Test
    void mapWithAffectation() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Déclarations des députés")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Souligner quelque chose d'important")
                        .anchor("#anchor")
                        .children(List.of(
                                ScrapedLogNode.builder()
                                        .title("M. Lucien Bouchard")
                                        .paragraphs(List.of("M. Bouchard : Bonjour", "• (10 h 20) •", "Mais, lorsqu'on leur demande : Qu'allez-vous faire?", "oui", "Vice-Président : merci lulu", "bon passons à autre chose"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("Document déposé")
                                        .build()
                        ))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Mais, lorsqu'on leur demande : Qu'allez-vous faire?", "oui", "Vice-Président : merci lulu", "bon passons à autre chose"))
                                .build()))
                .build();
        when(deputyFinderMock.findByCompleteName("M. Lucien Bouchard")).thenReturn(assignment);
        List<SubjectDetails> subjects = this.templateAImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    @Test
    void mapWithoutAffectation() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Présentation de projets de loi")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Projet de loi 954968489 - Interdiction de laisser un ski-doo dans sa cour.")
                        .anchor("#anchor")
                        .children(List.of(
                                ScrapedLogNode.builder()
                                        .title("M. Jacques Parizeau")
                                        .paragraphs(List.of("M. Parizeau : Ish", "• (10 h 20) •", "En tout cas"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("Mise aux voix")
                                        .paragraphs(List.of("125 pour, 0 contre."))
                                        .build()
                        )).build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Ish", "En tout cas"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise aux voix")
                                .paragraphs(List.of("125 pour, 0 contre."))
                                .build())
                ).build();
        when(deputyFinderMock.findByCompleteName("M. Jacques Parizeau")).thenReturn(assignment);
        List<SubjectDetails> subjects = this.templateAImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    private static class TemplateAImpl extends TemplateA {

        public TemplateAImpl(DeputyFinder deputyFinder) {
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
            return SubjectType.DEPUTY_DECLARATION;
        }
    }
}
