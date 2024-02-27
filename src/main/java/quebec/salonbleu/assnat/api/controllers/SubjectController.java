package quebec.salonbleu.assnat.api.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;

import java.util.Set;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.subjects.path}")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public SujetReponse getSubjectsByDeputyIds(@RequestParam @Size(min = 1, max = 125) Set<@NotBlank String> deputeIds,
                                               @RequestParam(required = false) @NotNull @Min(0) Integer page,
                                               @RequestParam(required = false) @NotNull @Min(5) @Max(25) Integer taille) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjectsByDeputyIds(deputeIds, page, taille))
                .build();
    }

    @GetMapping("/{ids}")
    public SujetReponse getSubjects(@PathVariable @Size(min = 1, max = 5) Set<@NotBlank String> ids) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(ids))
                .build();
    }
}
