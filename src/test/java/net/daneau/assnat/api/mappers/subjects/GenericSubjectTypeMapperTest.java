package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.api.models.subjects.SujetType;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

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
        DirectoryDTO directoryDTO = DirectoryDTO.builder()
                .circonscriptions(Map.of("districtId", Circonscription.builder().build()))
                .deputies(Map.of("deputyId", Depute.builder().build()))
                .partis(Map.of("partyId", Parti.builder().build()))
                .build();
        SubjectDetails subjectDetails = SubjectDetails.builder()
                .type(SubjectType.DEPUTY_DECLARATION)
                .title("title")
                .interventions(List.of(InterventionDocument.builder()
                        .districtId("districtId")
                        .partyId("partyId")
                        .deputyId("deputyId")
                        .paragraphs(List.of("bla bla bla", "bla bla bla"))
                        .build()))
                .build();

        SujetDetails sujetDetails = this.genericSubjectTypeMapper.map(subjectDetails, directoryDTO);
        assertEquals(subjectDetails.getTitle(), sujetDetails.getTitre());
        assertEquals(subjectDetails.getInterventions().get(0).getParagraphs(), sujetDetails.getInterventions().get(0).getParagraphes());
        assertEquals(SujetType.DECLARATION_DEPUTE, sujetDetails.getType());
        assertSame(directoryDTO.getDeputies().get("deputyId"), sujetDetails.getInterventions().get(0).getDepute());
        assertSame(directoryDTO.getPartis().get("partyId"), sujetDetails.getInterventions().get(0).getParti());
        assertSame(directoryDTO.getCirconscriptions().get("districtId"), sujetDetails.getInterventions().get(0).getCirconscription());
    }

    @Test
    void supports() {
        assertTrue(this.genericSubjectTypeMapper.supports().contains(SubjectType.DEPUTY_DECLARATION));
    }
}