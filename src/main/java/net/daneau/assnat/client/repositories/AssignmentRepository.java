package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    List<Assignment> findByEndDate(LocalDate endDate);

    Optional<Assignment> findByDeputyIdAndDistrictIdAndPartyIdAndEndDate(String deputyId, String districtId, String partyId, LocalDate endDate);
}
