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
    void map() {
        when(typeMapper.map(SubjectType.DEPUTY_DECLARATION)).thenReturn(SujetType.DECLARATION_DEPUTE);
        Map<UUID, Affectation> affectations = Map.of(TestUUID.ID1, Affectation.builder().build());

        SubjectDetails subjectDetails = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title("title")
                .interventions(List.of(InterventionDocument.builder()
                        .assignmentId(TestUUID.ID1)
                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                        .build()))
                .build();

        SujetDetails sujetDetails = this.genericSubjectTypeMapper.map(subjectDetails, affectations);
        assertEquals(subjectDetails.getTitle(), sujetDetails.getTitre());
        assertEquals(subjectDetails.getInterventions().get(0).getParagraphs(), sujetDetails.getInterventions().get(0).getParagraphes());
        assertEquals(SujetType.DECLARATION_DEPUTE, sujetDetails.getType());
        assertSame(affectations.get(TestUUID.ID1), sujetDetails.getInterventions().get(0).getAffectation());
    }

    @Test
    void supports() {
        assertTrue(this.genericSubjectTypeMapper.supports().contains(SubjectType.DEPUTY_DECLARATION));
    }
}