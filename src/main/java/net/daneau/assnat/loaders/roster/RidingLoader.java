package net.daneau.assnat.loaders.roster;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Riding;
import net.daneau.assnat.client.repositories.RidingRepository;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class RidingLoader {

    private final RidingRepository ridingRepository;

    List<Riding> load(Iterable<ScrapedDeputy> scrapedDeputies) {
        List<Riding> ridings = this.ridingRepository.findAll();
        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            List<Riding> ridingResults = ridings.stream()
                    .filter(riding -> StringUtils.equals(riding.getName(), scrapedDeputy.getRiding()))
                    .toList();
            if (ridingResults.size() == 0) {
                ridings.add(
                        this.ridingRepository.save(
                                Riding.builder()
                                        .name(scrapedDeputy.getRiding())
                                        .build()));
            }
        }
        return Collections.unmodifiableList(ridings);
    }
}
