package quebec.salonbleu.assnat.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.parties.PartiReponse;
import quebec.salonbleu.assnat.api.services.PartyService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.party.path}")
public class PartyController {

    private final PartyService partyService;

    @GetMapping
    public PartiReponse getPartiesByName(@RequestParam String nom) {
        return PartiReponse.builder()
                .partis(this.partyService.getPartiesByName(nom))
                .build();
    }
}
