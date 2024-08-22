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
class TemplateCTest {

    @Mock
    private DeputyFinder deputyFinderMock;
    @InjectMocks
    private TemplateCImpl templateCImpl;

    @Test
    void map_simple() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Motions sans préavis")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Reconnaître que le Québec est un endroit sur la terre.")
                        .anchor("#anchor")
                        .children(List.of(
                                ScrapedLogNode.builder()
                                        .title("Document déposé")
                                        .paragraphs(List.of("M. Legault : Bonjour", "• (10 h 20) •", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("Mise au voix")
                                        .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                        .build()))
                        .build()))
                .build();
        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise au voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                .build())
                )
                .build();
        when(deputyFinderMock.findByLastName("M. Legault")).thenReturn(Optional.of(assignment));

        List<SubjectDetails> subjects = this.templateCImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    @Test
    void map_president() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Motions sans préavis")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Reconnaître que le Québec est un endroit sur la terre.")
                        .anchor("#anchor")
                        .paragraphs(List.of("La Présidente : Bonjour", "• (10 h 20) •", "Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                        .children(List.of(
                                ScrapedLogNode.builder()
                                        .title("M. Jacques Parizeau")
                                        .paragraphs(List.of("M. Parizeau : Hmmm?"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("La Présidente")
                                        .paragraphs(List.of("La Présidente : Allo", "Avez-vous vu mon chat?"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("Mise aux voix")
                                        .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                        .build()
                        ))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .note("La Présidente")
                                .paragraphs(List.of("Bonjour", "Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                .build(),
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Hmmm?"))
                                .build(),
                        InterventionDocument.builder()
                                .note("La Présidente")
                                .paragraphs(List.of("La Présidente : Allo", "Avez-vous vu mon chat?"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise aux voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                .build())
                )
                .build();
        when(deputyFinderMock.findByLastName("M. Parizeau")).thenReturn(Optional.of(assignment));

        List<SubjectDetails> subjects = this.templateCImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    @Test
    void map_document_depose() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Motions sans préavis")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Document déposé")
                        .anchor("#anchor")
                        .paragraphs(List.of("M. Landry : Bonjour", "• (10 h 20) •", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                        .children(List.of(ScrapedLogNode.builder()
                                .title("Mise au voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                .build()))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise au voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                .build())
                )
                .build();
        when(deputyFinderMock.findByLastName("M. Landry")).thenReturn(Optional.of(assignment));

        List<SubjectDetails> subjects = this.templateCImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    @Test
    void map_with_debate() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Motions sans préavis")
                .children(List.of(ScrapedLogNode.builder()
                        .title("Reconnaître que le Québec est un endroit sur la terre.")
                        .anchor("#anchor")
                        .paragraphs(List.of("M. Landry : Bonjour", "• (10 h 20) •", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                        .children(List.of(
                                ScrapedLogNode.builder()
                                        .title("M. Jacques Parizeau")
                                        .paragraphs(List.of("M. Parizeau : Hmmm?"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("M. René Lévesque")
                                        .paragraphs(List.of("M. Lévesque : Allo", "Avez-vous vu mon chat?"))
                                        .build(),
                                ScrapedLogNode.builder()
                                        .title("Mise aux voix")
                                        .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                        .build()
                        ))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                .build(),
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Hmmm?"))
                                .build(),
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Allo", "Avez-vous vu mon chat?"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise aux voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui"))
                                .build())
                )
                .build();
        when(deputyFinderMock.findByLastName("M. Landry")).thenReturn(Optional.of(assignment));
        when(deputyFinderMock.findByLastName("M. Lévesque")).thenReturn(Optional.of(assignment));
        when(deputyFinderMock.findByLastName("M. Parizeau")).thenReturn(Optional.of(assignment));

        List<SubjectDetails> subjects = this.templateCImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
    }

    @Test
    void map_with_rejectedMotion() {
        ScrapedLogNode scrapedLogNode = ScrapedLogNode.builder()
                .title("Motions sans préavis")
                .anchor("#anchor3")
                .paragraphs(List.of(
                        "Bon maintenant passons au député de La Peltrie",
                        "Mme Marois : Yo les jeunes",
                        "«Reconnaître que l'eau est mouillée;",
                        "«Que le feu est chaud;",
                        "«et que la neige est froide.»",
                        "M. Caire : Pas de consentement"
                ))
                .children(List.of(ScrapedLogNode.builder()
                        .title("Reconnaître que le Québec est un endroit sur la terre.")
                        .anchor("#anchor")
                        .paragraphs(List.of("M. Landry : Bonjour", "• (10 h 20) •", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                        .children(List.of(ScrapedLogNode.builder()
                                .title("Mise aux voix")
                                .anchor("#anchor2")
                                .paragraphs(List.of(
                                        "La Présidente : Cette motion est-elle adoptée?",
                                        "Oui",
                                        "Bon maintenant passons au député de Camille-Laurin",
                                        "M. St-Pierre-Plamondon : Salut gang!",
                                        "«Reconnaître que le Québec est un pays;",
                                        "«et qu'on peut laisser un ski-doo dans sa cour»",
                                        "M. Legault : Lol",
                                        "M. Caire : Pas de consentement",
                                        "Bon maintenant passons au député de Matane-Matapédia",
                                        "M. Bérubé (Matane-Matapédia) : Ok je vais réessayer sans le ski-doo voir.",
                                        "J'aimerais déposé la motion suivante :",
                                        "«Reconnaître que le Québec est vraiment un pays»",
                                        "M. Legault : Attendez que je ne sois plus chef de la caq et on s'en reparlera.",
                                        "M. Caire : Pas de consentement"
                                ))
                                .build()))
                        .build()))
                .build();

        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        SubjectDetails expectedResult = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title(scrapedLogNode.getChildren().getFirst().getTitle())
                .anchor(scrapedLogNode.getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("Bonjour", "Merci, Mme la Présidente. Je sollicite le consentement de cette Assemblée pour présenter la motion suivante"))
                                .build(),
                        InterventionDocument.builder()
                                .note("Mise aux voix")
                                .paragraphs(List.of("La Présidente : Cette motion est-elle adoptée?", "Oui", "Bon maintenant passons au député de Camille-Laurin"))
                                .build())
                )
                .build();

        SubjectDetails expectedRejectedMotion1 = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title("Motion rejetée")
                .anchor(scrapedLogNode.getChildren().getFirst().getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("«Reconnaître que le Québec est un pays;", "«et qu'on peut laisser un ski-doo dans sa cour»"))
                                .build()
                )).build();
        SubjectDetails expectedRejectedMotion2 = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title("Motion rejetée")
                .anchor(scrapedLogNode.getChildren().getFirst().getChildren().getFirst().getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of("«Reconnaître que le Québec est vraiment un pays»"))
                                .build()
                )).build();
        SubjectDetails expectedRejectedMotion3 = SubjectDetails.builder()
                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                .title("Motion rejetée")
                .anchor(scrapedLogNode.getAnchor())
                .interventions(List.of(
                        InterventionDocument.builder()
                                .assignmentId(assignment.getId())
                                .districtId(assignment.getDistrictId())
                                .partyId(assignment.getPartyId())
                                .deputyId(assignment.getDeputyId())
                                .paragraphs(List.of(
                                        "«Reconnaître que l'eau est mouillée;",
                                        "«Que le feu est chaud;",
                                        "«et que la neige est froide.»"
                                )).build()
                )).build();
        when(deputyFinderMock.findByLastName("M. Landry")).thenReturn(Optional.of(assignment));
        when(deputyFinderMock.findByLastName("Mme Marois")).thenReturn(Optional.of(assignment));
        when(deputyFinderMock.findByLastName("M. St-Pierre-Plamondon")).thenReturn(Optional.of(assignment));
        when(deputyFinderMock.findByLastNameAndDistrict("M. Bérubé", "Matane-Matapédia")).thenReturn(Optional.of(assignment));

        List<SubjectDetails> subjects = this.templateCImpl.map(scrapedLogNode);
        assertEquals(expectedResult, subjects.getFirst());
        assertEquals(expectedRejectedMotion1, subjects.get(1));
        assertEquals(expectedRejectedMotion2, subjects.get(2));
        assertEquals(expectedRejectedMotion3, subjects.get(3));
    }

    private static class TemplateCImpl extends TemplateC {

        public TemplateCImpl(DeputyFinder deputyFinder) {
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
            return SubjectType.MOTION_WITHOUT_NOTICE;
        }
    }
}