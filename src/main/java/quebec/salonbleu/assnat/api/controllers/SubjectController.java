package quebec.salonbleu.assnat.api.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;

import java.util.Set;
import java.util.UUID;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.subjects.path}")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public SujetReponse getSubjectsByDeputyIds(@RequestParam(required = false) String motsCles,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 125) Set<UUID> deputeIds,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 5) Set<UUID> partiIds,
                                               @RequestParam(required = false, defaultValue = "") @Size(max = 125) Set<UUID> circonscriptionIds,
                                               @RequestParam(required = false, defaultValue = "0") @Min(0) Integer page,
                                               @RequestParam(required = false, defaultValue = "25") @Min(1) @Max(25) Integer taille) {
        SubjectArgs args = SubjectArgs.builder()
                .searchString(motsCles)
                .deputyIds(deputeIds)
                .partyIds(partiIds)
                .districtIds(circonscriptionIds)
                .build();
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(args, page, taille))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .build();
    }

    @GetMapping("/{ids}")
    public SujetReponse getSubjects(@PathVariable @Size(min = 1, max = 5) Set<UUID> ids) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(ids))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .build();
    }
}
