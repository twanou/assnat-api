package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Deputy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeputyRepository extends MongoRepository<Deputy, String> {

}
