package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.assignments.AffectationReponse;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.services.AssignmentService;
import quebec.salonbleu.assnat.loaders.services.LoadingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentServiceMock;
    @Mock
    private LoadingService loadingServiceMock;
    @InjectMocks
    private AssignmentController assignmentController;

    @Test
    void getCurrentAssignments() {
        List<Affectation> affectations = List.of(Affectation.builder().build());
        when(assignmentServiceMock.getCurrentAssignments()).thenReturn(affectations);

        AffectationReponse response = this.assignmentController.getCurrentAssignments();
        assertSame(affectations, response.getAffectations());
        verify(loadingServiceMock).load();
    }
}