package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.deputies.DeputeReponse;
import quebec.salonbleu.assnat.api.services.DeputyService;

@Tag(name = "4. Utilitaires")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.deputy.path}")
public class DeputyController {

    private final DeputyService deputyService;

    @Operation(summary = "Obtenir les député(e)s correspondants à partir d'un nom partiel.")
    @GetMapping
    public DeputeReponse getDeputiesByName(@RequestParam String nom) {
        return DeputeReponse.builder()
                .deputes(this.deputyService.getDeputiesByName(nom))
                .build();
    }
}
