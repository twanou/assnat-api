package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;
import test.utils.TestUUID;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    @Mock
    private SubjectService subjectServiceMock;
    @InjectMocks
    private SubjectController subjectController;

    @Test
    void getSubjectsByDeputyIds() {
        List<Sujet> sujets = List.of(Sujet.builder().build());
        when(subjectServiceMock.getSubjectsByDeputyIds(Set.of(TestUUID.ID1, TestUUID.ID2), 0, 25)).thenReturn(sujets);
        when(subjectServiceMock.getLastUpdate()).thenReturn(LocalDate.now());

        SujetReponse response = this.subjectController.getSubjectsByDeputyIds(Set.of(TestUUID.ID1, TestUUID.ID2), 0, 25);
        assertEquals(response.getSujets(), sujets);
        assertEquals(LocalDate.now(), response.getDerniereMaj());
    }

    @Test
    void getSubjects() {
        List<Sujet> sujets = List.of(Sujet.builder().build());
        when(subjectServiceMock.getSubjects(Set.of(TestUUID.ID1, TestUUID.ID2))).thenReturn(sujets);
        when(subjectServiceMock.getLastUpdate()).thenReturn(LocalDate.now());

        SujetReponse response = this.subjectController.getSubjects(Set.of(TestUUID.ID1, TestUUID.ID2));
        assertEquals(sujets, response.getSujets());
        assertEquals(LocalDate.now(), response.getDerniereMaj());
    }
}