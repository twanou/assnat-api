package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.Subject;

import java.util.Optional;
import java.util.UUID;

public interface SubjectSpringRepository extends MongoRepository<Subject, UUID> {

    Optional<Subject> findFirstByOrderByDateDesc();
}
