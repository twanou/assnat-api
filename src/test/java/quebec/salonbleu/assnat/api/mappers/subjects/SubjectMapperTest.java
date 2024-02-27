package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

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

    @Test
    void toSujetsList() {
        LocalDate now = LocalDate.now();
        List<Subject> subjects = List.of(Subject.builder()
                .id("id")
                .date(now)
                .legislature(1)
                .session(2)
                .pageId("123456")
                .subjectDetails(SubjectDetails.builder().type(SubjectType.DEPUTY_DECLARATION).anchor("#anchor").build())
                .build()
        );
        Map<String, Affectation> affectations = Map.of();
        SujetDetails sujetDetails = SujetDetails.builder().build();
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        when(subjectTypeMapperMock.map(subjects.get(0).getSubjectDetails(), affectations)).thenReturn(sujetDetails);
        when(assnatLinkBuilderMock.getUrl(1, 2, now, "123456", "#anchor")).thenReturn("url");
        SubjectMapper subjectMapper = new SubjectMapper(List.of(subjectTypeMapperMock), assnatLinkBuilderMock);
        List<Sujet> sujets = subjectMapper.toSujetsList(subjects, affectations);
        assertEquals(subjects.get(0).getSession(), sujets.get(0).getSession());
        assertEquals(subjects.get(0).getId(), sujets.get(0).getId());
        assertEquals(subjects.get(0).getLegislature(), sujets.get(0).getLegislature());
        assertEquals(subjects.get(0).getDate(), sujets.get(0).getDate());
        assertEquals("url", sujets.get(0).getUrl());
        assertSame(sujetDetails, sujets.get(0).getDetails());
    }

    @Test
    void missingMapperException() {
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of(), assnatLinkBuilderMock));
    }

    @Test
    void duplicateMapperException() {
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of(subjectTypeMapperMock, subjectTypeMapperMock), assnatLinkBuilderMock));
    }
}