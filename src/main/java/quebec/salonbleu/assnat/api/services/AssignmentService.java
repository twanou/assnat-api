package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import quebec.salonbleu.assnat.cache.CacheKey;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.utils.PhotoUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final DeputyService deputyService;
    private final DistrictService districtService;
    private final PartyService partyService;
    private final PhotoUtils photoUtils;
    private final AssignmentRepository assignmentRepository;

    @Cacheable(CacheKey.Constants.CURRENT_ASSIGNMENTS)
    public List<Affectation> getCurrentAssignments() {
        return this.getAssignments(this.assignmentRepository.findByEndDate(null))
                .values().stream()
                .sorted(Comparator.comparing(affectation -> StringUtils.stripAccents(affectation.getDepute().getNom() + affectation.getDepute().getPrenom())))
                .toList();
    }

    @Cacheable(CacheKey.Constants.ALL_ASSIGNMENTS)
    public Map<UUID, Affectation> getAllAssignments() {
        return this.getAssignments(this.assignmentRepository.findAll());
    }

    private Map<UUID, Affectation> getAssignments(List<Assignment> assignments) {
        Map<UUID, Depute> deputes = this.deputyService.getDeputies();
        Map<UUID, Circonscription> circonscriptions = this.districtService.getDistricts();
        Map<UUID, Parti> partis = this.partyService.getParties();

        return assignments
                .stream()
                .collect(Collectors.toUnmodifiableMap(Assignment::getId,
                        assignment -> Affectation.builder()
                                .depute(deputes.get(assignment.getDeputyId()))
                                .parti(partis.get(assignment.getPartyId()))
                                .circonscription(circonscriptions.get(assignment.getDistrictId()))
                                .photoUrl(this.getPhotoUrl(
                                        deputes.get(assignment.getDeputyId()),
                                        circonscriptions.get(assignment.getDistrictId()))
                                )
                                .fonctions(assignment.getFunctions())
                                .build()));
    }

    private String getPhotoUrl(Depute depute, Circonscription circonscription) {
        return this.photoUtils.getPhotoUrl(depute.getPrenom(), depute.getNom(), circonscription.getNom());
    }
}
