package quebec.salonbleu.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.repositories.DistrictRepository;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class DistrictLoader {

    private final DistrictRepository districtRepository;

    List<District> load(Iterable<ScrapedDeputy> scrapedDeputies) {
        List<District> districts = this.districtRepository.findAll();
        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            List<District> districtResults = districts.stream()
                    .filter(district -> StringUtils.equals(district.getName(), scrapedDeputy.getDistrict()))
                    .toList();
            if (districtResults.isEmpty()) {
                districts.add(
                        this.districtRepository.save(
                                District.builder()
                                        .name(scrapedDeputy.getDistrict())
                                        .build()));
            }
        }
        return Collections.unmodifiableList(districts);
    }
}
