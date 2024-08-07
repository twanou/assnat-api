package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.subjects.requests.SujetRequete;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;

import java.util.Set;
import java.util.UUID;


@Tag(name = "3. Sujets")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.subject.path}")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "Obtenir les sujets selon des critères de recherche.")
    @PostMapping
    public SujetReponse getSubjects(@RequestBody @Valid SujetRequete sujetRequete) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(sujetRequete))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .chargementEnCours(this.subjectService.getCurrentlyLoading())
                .build();
    }

    @Operation(summary = "Obtenir les sujets selon leur identifiant.")
    @GetMapping("/{ids}")
    public SujetReponse getSubjectsById(@PathVariable @Size(min = 1, max = 1) Set<UUID> ids) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjectsById(ids))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .chargementEnCours(this.subjectService.getCurrentlyLoading())
                .build();
    }
}
