package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectMapperTest {

    @Mock
    private SubjectTypeMapper subjectTypeMapperMock;

    @Test
    void toSujetsList() {
        List<Subject> subjects = List.of(Subject.builder()
                .id("id")
                .date(LocalDate.now())
                .legislature(1)
                .session(2)
                .subjectDetails(SubjectDetails.builder().type(SubjectType.DEPUTY_DECLARATION).build())
                .build()
        );
        DirectoryDTO directoryDTO = DirectoryDTO.builder().build();
        SujetDetails sujetDetails = SujetDetails.builder().build();
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        when(subjectTypeMapperMock.map(subjects.get(0).getSubjectDetails(), directoryDTO)).thenReturn(sujetDetails);

        SubjectMapper subjectMapper = new SubjectMapper(List.of(subjectTypeMapperMock));
        List<Sujet> sujets = subjectMapper.toSujetsList(subjects, directoryDTO);
        assertEquals(subjects.get(0).getSession(), sujets.get(0).getSession());
        assertEquals(subjects.get(0).getLegislature(), sujets.get(0).getLegislature());
        assertEquals(subjects.get(0).getDate(), sujets.get(0).getDate());
        assertSame(sujetDetails, sujets.get(0).getDetails());
    }

    @Test
    void missingMapperException() {
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of()));
    }

    @Test
    void duplicateMapperException() {
        when(subjectTypeMapperMock.supports()).thenReturn(EnumSet.allOf(SubjectType.class));
        assertThrows(IllegalStateException.class, () -> new SubjectMapper(List.of(subjectTypeMapperMock, subjectTypeMapperMock)));
    }
}