package quebec.salonbleu.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class DeputyLoader {

    private final DeputyRepository deputyRepository;

    List<Deputy> load(Iterable<ScrapedDeputy> scrapedDeputies) {
        List<Deputy> deputies = this.deputyRepository.findAll();
        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            List<Deputy> deputyResults = deputies.stream()
                    .filter(deputy -> StringUtils.equals(deputy.getFirstName(), scrapedDeputy.getFirstName()))
                    .filter(deputy -> StringUtils.equals(deputy.getLastName(), scrapedDeputy.getLastName()))
                    .toList();
            if (deputyResults.isEmpty()) {
                deputies.add(
                        this.deputyRepository.save(
                                Deputy.builder()
                                        .title(scrapedDeputy.getTitle())
                                        .firstName(scrapedDeputy.getFirstName())
                                        .lastName(scrapedDeputy.getLastName())
                                        .build()));
            }
        }
        return Collections.unmodifiableList(deputies);
    }
}
