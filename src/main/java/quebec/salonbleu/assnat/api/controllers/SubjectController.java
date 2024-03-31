package quebec.salonbleu.assnat.api.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.subjects.path}")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @GetMapping
    public SujetReponse getSubjectsByDeputyIds(@RequestParam(required = false) Optional<String> motsCles,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 125) Set<UUID> deputeIds,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 5) Set<UUID> partyIds,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 125) Set<UUID> districtIds,
                                               @RequestParam @NotNull @Min(0) Integer page,
                                               @RequestParam @NotNull @Min(5) @Max(25) Integer taille) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjectsByDeputyIds(deputeIds, page, taille))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .build();
    }

    @GetMapping("/{ids}")
    public SujetReponse getSubjects(@PathVariable @Size(min = 1, max = 5) Set<UUID> ids) {
        this.subjectRepository.search("patate", List.of(UUID.fromString("04dd1ec2-d2e9-4af7-961f-8e93efb881ef"), UUID.fromString("750b1e7a-321b-4bd5-8a87-438df0713631")), List.of(), List.of());
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(ids))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .build();
    }
}
