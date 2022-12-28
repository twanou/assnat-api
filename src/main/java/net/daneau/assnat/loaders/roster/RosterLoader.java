package net.daneau.assnat.loaders.roster;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.documents.Riding;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.repositories.RosterRepository;
import net.daneau.assnat.loaders.events.RosterUpdateEvent;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.scrappers.DeputyScraper;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RosterLoader {

    private final ErrorHandler errorHandler;
    private final DeputyScraper deputyScraper;
    private final DeputyLoader deputyLoader;
    private final PartyLoader partyLoader;
    private final RidingLoader ridingLoader;
    private final RosterRepository rosterRepository;
    private final ApplicationEventPublisher eventBus;

    public void load() {
        List<ScrapedDeputy> scrapedDeputies = this.deputyScraper.scrape();
        Optional<Roster> currentRoster = this.rosterRepository.findByEndDate(null);

        int newHash = scrapedDeputies.hashCode();

        if (newHash != currentRoster.map(Roster::getHash).orElse(0)) {
            List<Deputy> deputies = this.deputyLoader.load(scrapedDeputies);
            List<Party> parties = this.partyLoader.load(scrapedDeputies);
            List<Riding> ridings = this.ridingLoader.load(scrapedDeputies);
            List<Assignment> assignments = new ArrayList<>();
            for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
                Deputy deputy = this.getDeputy(scrapedDeputy, deputies);
                Riding riding = this.getRiding(scrapedDeputy, ridings);
                Party party = this.getParty(scrapedDeputy, parties);
                assignments.add(
                        Assignment.builder()
                                .deputyId(deputy.getId())
                                .ridingId(riding.getId())
                                .partyId(party.getId())
                                .build()
                );
            }
            this.rosterRepository.save(
                    Roster.builder()
                            .hash(newHash)
                            .startDate(LocalDate.now())
                            .assignments(assignments)
                            .build()
            );
            currentRoster.ifPresent(roster -> this.rosterRepository.save(roster.withEndDate(LocalDate.now())));
            this.eventBus.publishEvent(new RosterUpdateEvent(this));
        }
    }

    private Deputy getDeputy(ScrapedDeputy scrapedDeputy, List<Deputy> deputies) {
        List<Deputy> deputyResults = deputies.stream()
                .filter(d -> StringUtils.equals(d.getFirstName(), scrapedDeputy.getFirstName()))
                .filter(d -> StringUtils.equals(d.getLastName(), scrapedDeputy.getLastName()))
                .toList();
        this.errorHandler.assertLessThan(2, deputyResults, () -> new LoadingException("+ de 2 députés avec le même nom!"));
        return deputyResults.get(0);
    }

    private Riding getRiding(ScrapedDeputy scrapedDeputy, List<Riding> ridings) {
        return ridings.stream()
                .filter(r -> StringUtils.equals(r.getName(), scrapedDeputy.getRiding()))
                .findFirst()
                .orElseThrow();
    }

    private Party getParty(ScrapedDeputy scrapedDeputy, List<Party> parties) {
        return parties.stream()
                .filter(p -> StringUtils.equals(p.getName(), scrapedDeputy.getParty()))
                .findFirst()
                .orElseThrow();
    }
}
