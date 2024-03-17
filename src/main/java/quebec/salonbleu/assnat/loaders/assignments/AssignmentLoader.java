package quebec.salonbleu.assnat.loaders.assignments;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.cache.AssnatCacheManager;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.scrapers.DeputyScraper;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AssignmentLoader {

    private final ErrorHandler errorHandler;
    private final DeputyScraper deputyScraper;
    private final DeputyLoader deputyLoader;
    private final PartyLoader partyLoader;
    private final DistrictLoader districtLoader;
    private final AssignmentRepository assignmentRepository;
    private final AssnatCacheManager assnatCacheManager;

    public void load() {
        List<ScrapedDeputy> scrapedDeputies = this.deputyScraper.scrape();
        this.assertNoDuplicateScrapedDeputies(scrapedDeputies);

        List<Assignment> currentAssignments = this.assignmentRepository.findByEndDate(null);
        List<ScrapedDeputy> newScrapedDeputies = this.getScrapedDeputiesNotInAssignment(scrapedDeputies, currentAssignments);
        List<Assignment> oldAssignments = this.getAssignmentNotInScrapedDeputies(scrapedDeputies, currentAssignments);

        List<Deputy> deputies = List.of();
        List<Party> parties = List.of();
        List<District> districts = List.of();
        if (!newScrapedDeputies.isEmpty()) {
            deputies = this.deputyLoader.load(scrapedDeputies);
            parties = this.partyLoader.load(scrapedDeputies);
            districts = this.districtLoader.load(scrapedDeputies);
        }

        for (ScrapedDeputy newScrapedDeputy : newScrapedDeputies) {
            Deputy deputy = this.getDeputy(newScrapedDeputy, deputies);
            District district = this.getDistrict(newScrapedDeputy, districts);
            Party party = this.getParty(newScrapedDeputy, parties);
            this.assignmentRepository.save(
                    Assignment.builder()
                            .startDate(LocalDate.now())
                            .hash(newScrapedDeputy.hashCode())
                            .deputyId(deputy.getId())
                            .districtId(district.getId())
                            .partyId(party.getId())
                            .photo(newScrapedDeputy.getPhoto())
                            .functions(newScrapedDeputy.getFunctions())
                            .build()
            );
        }

        for (Assignment oldAssignment : oldAssignments) {
            this.assignmentRepository.save(oldAssignment.withEndDate(LocalDate.now()));
        }

        if (!newScrapedDeputies.isEmpty() || !oldAssignments.isEmpty()) {
            this.assnatCacheManager.clearAllCaches();
        }
    }

    private List<ScrapedDeputy> getScrapedDeputiesNotInAssignment(List<ScrapedDeputy> scrapedDeputies, List<Assignment> assignments) {
        return scrapedDeputies.stream()
                .filter(scrapedDeputy -> assignments
                        .stream()
                        .noneMatch(assignment -> assignment.getHash() == scrapedDeputy.hashCode()))
                .toList();
    }

    private List<Assignment> getAssignmentNotInScrapedDeputies(List<ScrapedDeputy> scrapedDeputies, List<Assignment> assignments) {
        return assignments.stream()
                .filter(assignment -> scrapedDeputies
                        .stream()
                        .noneMatch(scrapedDeputy -> scrapedDeputy.hashCode() == assignment.getHash()))
                .toList();
    }

    private Deputy getDeputy(ScrapedDeputy scrapedDeputy, List<Deputy> deputies) {
        List<Deputy> deputyResults = deputies.stream()
                .filter(d -> StringUtils.equals(d.getFirstName(), scrapedDeputy.getFirstName()))
                .filter(d -> StringUtils.equals(d.getLastName(), scrapedDeputy.getLastName()))
                .toList();
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

    private void assertNoDuplicateScrapedDeputies(List<ScrapedDeputy> scrapedDeputies) {
        Set<ScrapedDeputy> elements = new HashSet<>();
        List<ScrapedDeputy> results = scrapedDeputies.stream()
                .filter(n -> !elements.add(n))
                .toList();

        //On verra ce qu'on fait si ça arrive!
        this.errorHandler.assertSize(0, results, () -> new LoadingException("Deux députés avec le même nom."));
    }
}
