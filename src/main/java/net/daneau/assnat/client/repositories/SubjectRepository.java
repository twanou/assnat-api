package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject, String> {

    Optional<Subject> findFirstByOrderByDateDesc();

    @Query("{'array':{ $elemMatch: { 'deputyId': { $in: ?0}}}}")
    List<Subject> findSubjectsByDeputyIds(Iterable<String> ids);
}

