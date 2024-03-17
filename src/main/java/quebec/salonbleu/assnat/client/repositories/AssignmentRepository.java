package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.Assignment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends MongoRepository<Assignment, UUID> {

    List<Assignment> findByEndDate(LocalDate endDate);
}
