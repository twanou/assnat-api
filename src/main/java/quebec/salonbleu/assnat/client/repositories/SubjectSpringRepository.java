package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import quebec.salonbleu.assnat.client.documents.Subject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SubjectSpringRepository extends MongoRepository<Subject, UUID> {

    Optional<Subject> findFirstByOrderByDateDesc();

    @Query(value = "{'subjectDetails.interventions':{ $elemMatch: { 'deputyId': { $in: ?0}}}}", sort = "{ date : -1 }")
    List<Subject> findSubjectsByDeputyIds(Set<UUID> ids, Pageable pageable);
}



