package net.daneau.assnat.api.controllers;

import net.daneau.assnat.api.models.assignments.responses.AffectationReponse;
import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.services.AssignmentService;
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
    void getCurrentRoster() {
        List<Affectation> affectations = List.of(Affectation.builder().build());
        when(assignmentServiceMock.getCurrentAssignments()).thenReturn(affectations);
        AffectationReponse response = this.assignmentController.getCurrentAssignments();
        assertEquals(affectations, response.getAffectations());
    }
}