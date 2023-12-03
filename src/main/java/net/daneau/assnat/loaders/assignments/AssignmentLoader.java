package net.daneau.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.District;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.loaders.events.AssignmentUpdateEvent;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.scrappers.DeputyScraper;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AssignmentLoader {

    private final ErrorHandler errorHandler;
    private final DeputyScraper deputyScraper;
    private final DeputyLoader deputyLoader;
    private final PartyLoader partyLoader;
    private final DistrictLoader districtLoader;
    private final AssignmentRepository assignmentRepository;
    private final ApplicationEventPublisher eventBus;

    public void load() {
        List<ScrapedDeputy> scrapedDeputies = this.deputyScraper.scrape();
        List<Assignment> currentAssignments = this.assignmentRepository.findByEndDate(null);

        boolean isUpdated = false;
        List<Deputy> deputies = List.of();
        List<Party> parties = List.of();
        List<District> districts = List.of();

        for (ScrapedDeputy scrapedDeputy : scrapedDeputies) {
            if (currentAssignments.stream().noneMatch(assignment -> assignment.getHash() == scrapedDeputy.hashCode())) {
                deputies = deputies.isEmpty() ? this.deputyLoader.load(scrapedDeputies) : deputies;
                parties = parties.isEmpty() ? this.partyLoader.load(scrapedDeputies) : parties;
                districts = districts.isEmpty() ? this.districtLoader.load(scrapedDeputies) : districts;

                Deputy deputy = this.getDeputy(scrapedDeputy, deputies);
                District district = this.getDistrict(scrapedDeputy, districts);
                Party party = this.getParty(scrapedDeputy, parties);
                Optional<Assignment> oldAssignment = this.assignmentRepository.findByDeputyIdAndEndDate(deputy.getId(), null);
                oldAssignment.ifPresent(old -> this.assignmentRepository.save(old.withEndDate(LocalDate.now())));
                this.assignmentRepository.save(
                        Assignment.builder()
                                .startDate(LocalDate.now())
                                .hash(scrapedDeputy.hashCode())
                                .deputyId(deputy.getId())
                                .districtId(district.getId())
                                .partyId(party.getId())
                                .functions(scrapedDeputy.getFunctions())
                                .build()
                );
                isUpdated = true;
            }
        }
        if (isUpdated) {
            this.eventBus.publishEvent(new AssignmentUpdateEvent(this));
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

    private District getDistrict(ScrapedDeputy scrapedDeputy, List<District> districts) {
        return districts.stream()
                .filter(r -> StringUtils.equals(r.getName(), scrapedDeputy.getDistrict()))
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
