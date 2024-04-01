package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;
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
        SubjectArgs args = SubjectArgs.builder()
                .searchString("mots")
                .deputyIds(Set.of(TestUUID.ID1, TestUUID.ID2))
                .districtIds(Set.of(TestUUID.ID3, TestUUID.ID4))
                .partyIds(Set.of(TestUUID.ID5, TestUUID.ID6))
                .build();
        when(subjectServiceMock.getSubjects(args, 0, 25)).thenReturn(sujets);
        when(subjectServiceMock.getLastUpdate()).thenReturn(LocalDate.now());
        when(subjectServiceMock.getNextUpdates()).thenReturn(List.of(LocalDate.now()));

        SujetReponse response = this.subjectController.getSubjectsByDeputyIds(args.getSearchString().get(), args.getDeputyIds(), args.getPartyIds(), args.getDistrictIds(), 0, 25);
        assertEquals(response.getSujets(), sujets);
        assertEquals(LocalDate.now(), response.getDerniereMaj());
        assertEquals(List.of(LocalDate.now()), response.getFuturesMaj());
    }

    @Test
    void getSubjects() {
        List<Sujet> sujets = List.of(Sujet.builder().build());
        when(subjectServiceMock.getSubjects(Set.of(TestUUID.ID1, TestUUID.ID2))).thenReturn(sujets);
        when(subjectServiceMock.getLastUpdate()).thenReturn(LocalDate.now());
        when(subjectServiceMock.getNextUpdates()).thenReturn(List.of(LocalDate.now()));

        SujetReponse response = this.subjectController.getSubjects(Set.of(TestUUID.ID1, TestUUID.ID2));
        assertEquals(sujets, response.getSujets());
        assertEquals(LocalDate.now(), response.getDerniereMaj());
        assertEquals(List.of(LocalDate.now()), response.getFuturesMaj());
    }
}