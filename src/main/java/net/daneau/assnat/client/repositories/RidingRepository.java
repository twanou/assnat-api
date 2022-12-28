package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Riding;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RidingRepository extends MongoRepository<Riding, String> {
}
