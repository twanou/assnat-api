package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Subject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SubjectRepository extends MongoRepository<Subject, String> {

    Optional<Subject> findFirstByOrderByDateDesc();

    @Query("{'subjectDetails.interventions':{ $elemMatch: { 'deputyId': { $in: ?0}}}}")
    List<Subject> findSubjectsByDeputyObjectIds(Set<ObjectId> ids);

    default List<Subject> findSubjectsByDeputyIds(Set<String> ids) {
        return this.findSubjectsByDeputyObjectIds(ids.stream()
                .filter(ObjectId::isValid)
                .map(ObjectId::new)
                .collect(Collectors.toSet()));
    }
}



