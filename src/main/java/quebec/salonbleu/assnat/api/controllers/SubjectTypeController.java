package quebec.salonbleu.assnat.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.types.SujetTypeReponse;
import quebec.salonbleu.assnat.api.services.SubjectTypeService;

@Tag(name = "4. Utilitaires")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.type.path}")
public class SubjectTypeController {

    private final SubjectTypeService subjectTypeService;

    @Operation(summary = "Obtenir les partis correspondants à partir d'un nom partiel.")
    @GetMapping
    public SujetTypeReponse getSubjectTypes() {
        return SujetTypeReponse.builder()
                .types(this.subjectTypeService.getSubjectTypes())
                .build();
    }
}
