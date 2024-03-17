package quebec.salonbleu.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class DeputyLoader {

    private final DeputyRepository deputyRepository;
    private final ErrorHandler errorHandler;

    List<Deputy> load(Iterable<ScrapedDeputy> scrapedDeputies) {
        List<Deputy> deputies = this.deputyRepository.findAll();
        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            final List<Deputy> deputyResults = deputies.stream()
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
                                        .lastDistrict(scrapedDeputy.getDistrict())
                                        .build()));
            } else {
                List<Deputy> refinedDeputyResults = deputyResults.stream()
                        .filter(deputy -> deputy.getLastDistrict().equals(scrapedDeputy.getDistrict()))
                        .toList();
                this.errorHandler.assertSize(1, refinedDeputyResults, () -> new LoadingException("Député nécéssite validation manuelle" + scrapedDeputy));
            }
        }
        return Collections.unmodifiableList(deputies);
    }
}
