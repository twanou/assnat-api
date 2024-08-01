package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.assignments.AffectationReponse;
import quebec.salonbleu.assnat.api.services.AssignmentService;

@Tag(name = "2. Affectations")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.assignment.path}")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(summary = "Obtenir les affectations des député(e)s en fonction actuellement.")
    @GetMapping
    public AffectationReponse getCurrentAssignments() {
        return AffectationReponse.builder()
                .affectations(this.assignmentService.getCurrentAssignments())
                .build();
    }
}
