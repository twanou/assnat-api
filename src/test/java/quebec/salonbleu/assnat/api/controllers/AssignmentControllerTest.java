package quebec.salonbleu.assnat.api.controllers;

import quebec.salonbleu.assnat.api.models.assignments.responses.AffectationReponse;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.services.AssignmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentServiceMock;
    @InjectMocks
    private AssignmentController assignmentController;

    @Test
    void getCurrentAssignments() {
        List<Affectation> affectations = List.of(Affectation.builder().build());
        when(assignmentServiceMock.getCurrentAssignments()).thenReturn(affectations);
        AffectationReponse response = this.assignmentController.getCurrentAssignments();
        assertEquals(affectations, response.getAffectations());
    }
}