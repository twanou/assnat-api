package quebec.salonbleu.assnat.api.mappers.subjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericSubjectTypeMapperTest {

    @Mock
    private TypeMapper typeMapper;
    @InjectMocks
    private GenericSubjectTypeMapper genericSubjectTypeMapper;

    @Test
    void completeMap() {
        when(typeMapper.map(SubjectType.DEPUTY_DECLARATION)).thenReturn(SujetType.DECLARATION_DEPUTE);
        Map<UUID, Affectation> affectations = Map.of(TestUUID.ID1, Affectation.builder().build());

        SubjectDetails subjectDetails = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title("title")
                .interventions(List.of(
                                InterventionDocument.builder()
                                        .assignmentId(TestUUID.ID1)
                                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                                        .build(),
                                InterventionDocument.builder()
                                        .note("note")
                                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                                        .build()
                        )
                ).build();

        SujetDetails sujetDetails = this.genericSubjectTypeMapper.completeMap(subjectDetails, affectations);
        assertEquals(subjectDetails.getTitle(), sujetDetails.getTitre());

        assertEquals(subjectDetails.getInterventions().getFirst().getParagraphs(), sujetDetails.getInterventions().getFirst().getParagraphes());
        assertEquals(subjectDetails.getInterventions().get(1).getParagraphs(), sujetDetails.getInterventions().get(1).getParagraphes());

        assertNull(sujetDetails.getInterventions().getFirst().getNote());
        assertEquals(subjectDetails.getInterventions().get(1).getNote(), sujetDetails.getInterventions().get(1).getNote());

        assertSame(affectations.get(TestUUID.ID1), sujetDetails.getInterventions().getFirst().getAffectation());
        assertNull(sujetDetails.getInterventions().get(1).getAffectation());

        assertEquals(SujetType.DECLARATION_DEPUTE, sujetDetails.getType());
    }

    @Test
    void partialMap() {
        when(typeMapper.map(SubjectType.DEPUTY_DECLARATION)).thenReturn(SujetType.DECLARATION_DEPUTE);
        Map<UUID, Affectation> affectations = Map.of(TestUUID.ID1, Affectation.builder().build());

        SubjectDetails subjectDetails = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title("title")
                .interventions(List.of(
                                InterventionDocument.builder()
                                        .assignmentId(TestUUID.ID1)
                                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                                        .build(),
                                InterventionDocument.builder()
                                        .note("note")
                                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                                        .build()
                        )
                ).build();

        SujetDetails sujetDetails = this.genericSubjectTypeMapper.partialMap(subjectDetails, affectations);
        assertEquals(subjectDetails.getTitle(), sujetDetails.getTitre());
        assertEquals(1, sujetDetails.getInterventions().size());
        assertEquals(0, sujetDetails.getInterventions().getFirst().getParagraphes().size());
        assertSame(affectations.get(TestUUID.ID1), sujetDetails.getInterventions().getFirst().getAffectation());
        assertEquals(SujetType.DECLARATION_DEPUTE, sujetDetails.getType());
    }

    @Test
    void supports() {
        assertTrue(this.genericSubjectTypeMapper.supports().containsAll(List.of(SubjectType.DEPUTY_DECLARATION, SubjectType.QUESTIONS_ANSWERS, SubjectType.PETITION, SubjectType.MINISTERIAL_DECLARATION, SubjectType.LAW_PROJECT_PRESENTATION)));
    }
}