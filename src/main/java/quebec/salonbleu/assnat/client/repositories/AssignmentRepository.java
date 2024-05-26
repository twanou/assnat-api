package quebec.salonbleu.assnat.client.repositories;

import quebec.salonbleu.assnat.client.documents.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    List<Assignment> findByEndDate(LocalDate endDate);

    Optional<Assignment> findByDeputyIdAndEndDate(String deputyId, LocalDate endDate);
}
