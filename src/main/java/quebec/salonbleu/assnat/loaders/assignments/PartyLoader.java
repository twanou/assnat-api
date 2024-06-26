package quebec.salonbleu.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.PartyRepository;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class PartyLoader {

    private final PartyRepository partyRepository;

    List<Party> load(Iterable<ScrapedDeputy> scrapedDeputies) {
        List<Party> parties = this.partyRepository.findAll();
        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            List<Party> partyResults = parties.stream()
                    .filter(party -> StringUtils.equals(party.getName(), scrapedDeputy.getParty()))
                    .toList();
            if (partyResults.isEmpty()) {
                parties.add(
                        this.partyRepository.save(
                                Party.builder()
                                        .name(scrapedDeputy.getParty())
                                        .acronym(scrapedDeputy.getPartyAcronym())
                                        .build()));
            }
        }
        return Collections.unmodifiableList(parties);
    }
}
