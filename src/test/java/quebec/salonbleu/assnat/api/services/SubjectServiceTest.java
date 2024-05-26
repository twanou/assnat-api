package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import quebec.salonbleu.assnat.api.mappers.subjects.SubjectMapper;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.client.repositories.UpcomingLogRepository;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;
import test.utils.TestUUID;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepositoryMock;
    @Mock
    private UpcomingLogRepository upcomingLogRepositoryMock;
    @Mock
    private AssignmentService assignmentServiceMock;
    @Mock
    private SubjectMapper subjectMapperMock;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void getSubjectsByDeputyIds() {
        SubjectArgs args = SubjectArgs.builder()
                .searchString("mots")
                .deputyIds(Set.of(TestUUID.ID1, TestUUID.ID2))
                .districtIds(Set.of(TestUUID.ID3, TestUUID.ID4))
                .partyIds(Set.of(TestUUID.ID5, TestUUID.ID6))
                .build();
        List<Subject> subjects = List.of(Subject.builder().build());
        List<Sujet> sujets = List.of(Sujet.builder().build());
        Map<UUID, Affectation> affectations = Map.of();
        when(subjectRepositoryMock.find(args, PageRequest.of(0, 25))).thenReturn(subjects);
        when(assignmentServiceMock.getAllAssignments()).thenReturn(affectations);
        when(subjectMapperMock.toSujetsList(same(subjects), same(affectations))).thenReturn(sujets);

        List<Sujet> response = this.subjectService.getSubjects(args, 0, 25);
        assertSame(sujets, response);
    }

    @Test
    void getSubjects() {
        Set<UUID> ids = Set.of(TestUUID.ID1, TestUUID.ID2);
        List<Subject> subjects = List.of(Subject.builder().build());
        List<Sujet> sujets = List.of(Sujet.builder().build());
        Map<UUID, Affectation> affectations = Map.of();
        when(subjectRepositoryMock.findAllById(ids)).thenReturn(subjects);
        when(assignmentServiceMock.getAllAssignments()).thenReturn(affectations);
        when(subjectMapperMock.toSujetsList(same(subjects), same(affectations))).thenReturn(sujets);

        List<Sujet> response = this.subjectService.getSubjects(ids);
        assertSame(sujets, response);
    }

    @Test
    void getLastUpdate() {
        when(subjectRepositoryMock.findFirstByOrderByDateDesc()).thenReturn(Optional.of(Subject.builder().date(LocalDate.now()).build()));

        LocalDate response = this.subjectService.getLastUpdate();
        assertEquals(LocalDate.now(), response);
    }

    @Test
    void getNextUpdates() {
        when(upcomingLogRepositoryMock.findAll(Sort.by(Sort.Direction.ASC, "date"))).thenReturn(List.of(UpcomingLog.builder().date(LocalDate.now()).build()));

        List<LocalDate> response = this.subjectService.getNextUpdates();
        assertEquals(LocalDate.now(), response.get(0));
    }
}
