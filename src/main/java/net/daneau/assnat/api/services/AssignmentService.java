package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.cache.CacheKey;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.utils.PhotoUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    public Collection<Affectation> getCurrentAssignments() {
        return this.getAssignments(this.assignmentRepository.findByEndDate(null)).values();
    }

    @Cacheable(CacheKey.Constants.ALL_ASSIGNMENTS)
    public Map<String, Affectation> getAllAssignments() {
        return this.getAssignments(this.assignmentRepository.findAll());
    }

    private Map<String, Affectation> getAssignments(List<Assignment> assignments) {
        Map<String, Depute> deputes = this.deputyService.getDeputies();
        Map<String, Circonscription> circonscriptions = this.districtService.getDistricts();
        Map<String, Parti> partis = this.partyService.getParties();

        return assignments
                .stream()
                .collect(Collectors.toUnmodifiableMap(Assignment::getId,
                        assignment -> Affectation.builder()
                                .depute(deputes.get(assignment.getDeputyId()))
                                .parti(partis.get(assignment.getPartyId()))
                                .circonscription(circonscriptions.get(assignment.getDistrictId()))
                                .photoUrl(this.getPhotoUrl(
                                        deputes.get(assignment.getDeputyId()),
                                        circonscriptions.get(assignment.getDistrictId()),
                                        partis.get(assignment.getPartyId()))
                                )
                                .fonctions(assignment.getFunctions())
                                .build()));
    }

    private String getPhotoUrl(Depute depute, Circonscription circonscription, Parti parti) {
        return this.photoUtils.getPhotoUrl(depute.getPrenom(),
                depute.getNom(),
                circonscription.getNom(),
                parti.getNom());
    }
}
