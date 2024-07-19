package quebec.salonbleu.assnat.api.mappers.subjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.subjects.requests.SujetRequete;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;
import test.utils.TestUUID;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectMapperTest {

    @Mock
    private SubjectTypeMapper subjectTypeMapperMock;
    @Mock
    private AssnatLinkBuilder assnatLinkBuilderMock;
    @Mock
    private TypeMapper typeMapperMock;

    @Test
    void toSujetsList() {
        LocalDate now = LocalDate.now();
        List<Subject> subjects = List.of(Subject.builder()
                .id(TestUUID.ID1)
                .date(now)
                .legislature(1)
                .session(2)
                .pageId("123456")
                .subjectDetails(SubjectDetails.builder().type(SubjectType.DEPUTY_DECLARATION).anchor("#anchor").build())
                .build()
        );
        Map<UUID, Affectation> affectations = Map.of();
        SujetDetails sujetDetails = SujetDetails.builder().build();
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        when(subjectTypeMapperMock.map(subjects.getFirst().getSubjectDetails(), affectations)).thenReturn(sujetDetails);
        when(assnatLinkBuilderMock.getUrl(1, 2, now, "123456", "#anchor")).thenReturn("url");
        SubjectMapper subjectMapper = new SubjectMapper(List.of(subjectTypeMapperMock), assnatLinkBuilderMock, typeMapperMock);

        List<Sujet> sujets = subjectMapper.toSujetsList(subjects, affectations);
        assertEquals(subjects.getFirst().getSession(), sujets.getFirst().getSession());
        assertEquals(subjects.getFirst().getId(), sujets.getFirst().getId());
        assertEquals(subjects.getFirst().getLegislature(), sujets.getFirst().getLegislature());
        assertEquals(subjects.getFirst().getDate(), sujets.getFirst().getDate());
        assertEquals("url", sujets.getFirst().getUrl());
        assertSame(sujetDetails, sujets.getFirst().getDetails());
    }

    @Test
    void toSubjectArgs() {
        SujetRequete sujetRequete = SujetRequete.builder()
                .motsCles(Set.of("patate"))
                .phrase("J'aime les patates")
                .partiIds(Set.of(TestUUID.ID1))
                .deputeIds(Set.of(TestUUID.ID2))
                .circonscriptionIds(Set.of(TestUUID.ID3))
                .sujetTypes(Set.of(SujetType.DECLARATION_DEPUTE))
                .build();
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        when(typeMapperMock.map(SujetType.DECLARATION_DEPUTE)).thenReturn(SubjectType.DEPUTY_DECLARATION);
        SubjectMapper subjectMapper = new SubjectMapper(List.of(subjectTypeMapperMock), assnatLinkBuilderMock, typeMapperMock);

        SubjectArgs subjectArgs = subjectMapper.toSubjectArgs(sujetRequete);
        assertEquals(sujetRequete.getMotsCles(), subjectArgs.getKeywords());
        assertEquals(sujetRequete.getPhrase(), subjectArgs.getPhrase());
        assertEquals(sujetRequete.getPartiIds(), subjectArgs.getPartyIds());
        assertEquals(sujetRequete.getDeputeIds(), subjectArgs.getDeputyIds());
        assertEquals(sujetRequete.getCirconscriptionIds(), subjectArgs.getDistrictIds());
        assertEquals(Set.of(SubjectType.DEPUTY_DECLARATION.name()), subjectArgs.getSubjectTypes());
    }

    @Test
    void missingMapperException() {
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of(), assnatLinkBuilderMock, typeMapperMock));
    }

    @Test
    void duplicateMapperException() {
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of(subjectTypeMapperMock, subjectTypeMapperMock), assnatLinkBuilderMock, typeMapperMock));
    }
}