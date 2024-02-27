package quebec.salonbleu.assnat.client.repositories;

import quebec.salonbleu.assnat.client.documents.Subject;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SubjectRepository extends MongoRepository<Subject, String> {

    Optional<Subject> findFirstByOrderByDateDesc();

    @Query(value = "{'subjectDetails.interventions':{ $elemMatch: { 'deputyId': { $in: ?0}}}}", sort = "{ date : -1 }")
    List<Subject> findSubjectsByDeputyObjectIds(Set<ObjectId> ids, Pageable pageable);

    default List<Subject> findSubjectsByDeputyIds(Set<String> ids, Pageable pageable) {
        return this.findSubjectsByDeputyObjectIds(ids.stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .collect(Collectors.toSet()), pageable);
    }
}



