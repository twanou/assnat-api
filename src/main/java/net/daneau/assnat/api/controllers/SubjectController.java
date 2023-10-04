package net.daneau.assnat.api.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.subjects.responses.SujetReponse;
import net.daneau.assnat.api.services.SubjectService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/sujets")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public SujetReponse getSubjects(@RequestParam @Size(min = 1, max = 125) Set<@NotBlank String> deputeIds) {
        return SujetReponse.builder()
                .sujets(this.subjectService.getSubjects(deputeIds))
                .build();
    }
}
