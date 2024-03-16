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
import java.util.List;

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
        List<Assignment> currentAssignments = this.assignmentRepository.findByEndDate(null);

        boolean isUpdated = false;
        List<Deputy> deputies = List.of();
        List<Party> parties = List.of();
        List<District> districts = List.of();

        for (ScrapedDeputy scrapedDeputy : this.getScrapedDeputiesNotInAssignment(scrapedDeputies, currentAssignments)) {
            deputies = deputies.isEmpty() ? this.deputyLoader.load(scrapedDeputies) : deputies;
            parties = parties.isEmpty() ? this.partyLoader.load(scrapedDeputies) : parties;
            districts = districts.isEmpty() ? this.districtLoader.load(scrapedDeputies) : districts;

            Deputy deputy = this.getDeputy(scrapedDeputy, deputies);
            District district = this.getDistrict(scrapedDeputy, districts);
            Party party = this.getParty(scrapedDeputy, parties);

            this.assignmentRepository.save(
                    Assignment.builder()
                            .startDate(LocalDate.now())
                            .hash(scrapedDeputy.hashCode())
                            .deputyId(deputy.getId())
                            .districtId(district.getId())
                            .partyId(party.getId())
                            .photo(scrapedDeputy.getPhoto())
                            .functions(scrapedDeputy.getFunctions())
                            .build()
            );
            isUpdated = true;
        }

        for (Assignment assignment : this.getAssignmentNotInScrapedDeputies(scrapedDeputies, currentAssignments)) {
            this.assignmentRepository.save(assignment.withEndDate(LocalDate.now()));
        }

        if (isUpdated) {
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
