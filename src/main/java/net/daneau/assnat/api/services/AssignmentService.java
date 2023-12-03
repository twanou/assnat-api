package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.repositories.AssignmentRepository;
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
    private final AssignmentRepository assignmentRepository;

    @Cacheable("currentAssignmentsCache")
    public Collection<Affectation> getCurrentAssignments() {
        return this.getAssignments(this.assignmentRepository.findByEndDate(null)).values();
    }

    @Cacheable("assignmentsCache")
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
                                .fonctions(assignment.getFunctions())
                                .build()));
    }
}
