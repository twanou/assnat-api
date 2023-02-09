package net.daneau.assnat.api.controllers;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.roster.responses.CompositionReponse;
import net.daneau.assnat.api.services.RosterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compositions")
public class RosterController {

    private final RosterService rosterService;

    @GetMapping
    public CompositionReponse getCurrentRoster() {
        return CompositionReponse.builder()
                .compositions(this.rosterService.getCurrentRoster())
                .build();
    }
}
