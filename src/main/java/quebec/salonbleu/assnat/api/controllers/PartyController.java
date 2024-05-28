package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.parties.PartiReponse;
import quebec.salonbleu.assnat.api.services.PartyService;

@Tag(name = "4. Utilitaires")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.party.path}")
public class PartyController {

    private final PartyService partyService;

    @Operation(summary = "Obtenir les partis correspondants à partir d'un nom partiel.")
    @GetMapping
    public PartiReponse getPartiesByName(@RequestParam String nom) {
        return PartiReponse.builder()
                .partis(this.partyService.getPartiesByName(nom))
                .build();
    }
}
