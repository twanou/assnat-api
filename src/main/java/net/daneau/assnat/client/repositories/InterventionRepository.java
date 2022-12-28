package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Intervention;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InterventionRepository extends MongoRepository<Intervention, String> {

    Optional<Intervention> findFirstByOrderByDateDesc();
}
