package quebec.salonbleu.assnat.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.assignments.AffectationReponse;
import quebec.salonbleu.assnat.api.services.AssignmentService;
import quebec.salonbleu.assnat.loaders.services.LoadingService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.assignment.path}")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final LoadingService loadingService;

    @GetMapping
    public AffectationReponse getCurrentAssignments() {
        this.loadingService.load();
        return AffectationReponse.builder()
                .affectations(this.assignmentService.getCurrentAssignments())
                .build();
    }
}
