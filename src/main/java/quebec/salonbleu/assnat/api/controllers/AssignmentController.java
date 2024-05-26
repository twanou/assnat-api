package quebec.salonbleu.assnat.api.controllers;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.api.models.assignments.responses.AffectationReponse;
import quebec.salonbleu.assnat.api.services.AssignmentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.assignments.path}")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public AffectationReponse getCurrentAssignments() {
        return AffectationReponse.builder()
                .affectations(this.assignmentService.getCurrentAssignments())
                .build();
    }
}
