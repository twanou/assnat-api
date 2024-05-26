package quebec.salonbleu.assnat.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quebec.salonbleu.assnat.api.models.districts.CirconscriptionReponse;
import quebec.salonbleu.assnat.api.services.DistrictService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.district.path}")
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    public CirconscriptionReponse getDistrictsByName(@RequestParam String nom) {
        return CirconscriptionReponse.builder()
                .circonscriptions(this.districtService.getDistrictsByName(nom))
                .build();
    }
}
