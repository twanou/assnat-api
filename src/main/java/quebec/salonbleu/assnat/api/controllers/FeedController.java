package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.subjects.responses.SujetReponse;
import quebec.salonbleu.assnat.api.services.SubjectService;
import quebec.salonbleu.assnat.loaders.services.LoadingService;

import java.util.Set;
import java.util.UUID;

@Tag(name = "1. Fil")
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.feed.path}")
public class FeedController {

    private final SubjectService subjectService;
    private final LoadingService loadingService;

    @Operation(summary = "Obtenir les derniers sujets en fonction des député(e)s choisi(e)s.")
    @GetMapping
    public SujetReponse getSubjectsByDeputyIdsOrSubjectTypes(@RequestParam(required = false, defaultValue = "") @Size(max = 125) Set<UUID> deputeIds,
                                                             @RequestParam(required = false, defaultValue = "") Set<SujetType> sujetTypes,
                                                             @RequestParam @NotNull @Min(0) Integer page,
                                                             @RequestParam @NotNull @Min(5) @Max(25) Integer taille) {
        this.loadingService.load();
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjectsByDeputyIdsOrSubjectTypes(deputeIds, sujetTypes, page, taille))
                .derniereMaj(this.subjectService.getLastUpdate())
                .futuresMaj(this.subjectService.getNextUpdates())
                .chargementEnCours(this.subjectService.getCurrentlyLoading())
                .build();
    }
}
