package net.daneau.assnat.api.services;

import net.daneau.assnat.api.mappers.subjects.SubjectMapper;
import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepositoryMock;
    @Mock
    private AssignmentService assignmentServiceMock;
    @Mock
    private SubjectMapper subjectMapperMock;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void getSubjects() {
        Set<String> ids = Set.of("1", "2");
        List<Subject> subjects = List.of(Subject.builder().build());
        List<Sujet> sujets = List.of(Sujet.builder().build());
        Map<String, Affectation> affectations = Map.of();
        when(subjectRepositoryMock.findSubjectsByDeputyIds(ids)).thenReturn(subjects);
        when(assignmentServiceMock.getAllAssignments()).thenReturn(affectations);
        when(subjectMapperMock.toSujetsList(same(subjects), same(affectations))).thenReturn(sujets);

        List<Sujet> response = this.subjectService.getSubjects(ids);
        assertSame(sujets, response);
    }
}
