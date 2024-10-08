package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;
import quebec.salonbleu.assnat.loaders.services.LoadingService;
import test.utils.TestUUID;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {

    @Mock
    private SubjectService subjectServiceMock;
    @Mock
    private LoadingService loadingServiceMock;
    @InjectMocks
    private FeedController feedController;

    @Test
    void getSubjectsByDeputyIds() {
        List<Sujet> sujets = List.of(Sujet.builder().build());
        when(subjectServiceMock.getSubjectsByDeputyIdsOrSubjectTypes(Set.of(TestUUID.ID1, TestUUID.ID2), Set.of(SujetType.QUESTIONS_REPONSES, SujetType.MOTION_SANS_PREAVIS), 0, 25)).thenReturn(sujets);
        when(subjectServiceMock.getLastUpdate()).thenReturn(LocalDate.now());
        when(subjectServiceMock.getNextUpdates()).thenReturn(List.of(LocalDate.now()));

        SujetReponse response = this.feedController.getSubjectsByDeputyIdsOrSubjectTypes(Set.of(TestUUID.ID1, TestUUID.ID2), Set.of(SujetType.QUESTIONS_REPONSES, SujetType.MOTION_SANS_PREAVIS), 0, 25);
        assertEquals(response.getSujets(), sujets);
        assertEquals(LocalDate.now(), response.getDerniereMaj());
        assertEquals(List.of(LocalDate.now()), response.getFuturesMaj());
        verify(loadingServiceMock).load();
    }
}