package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.Party;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyRepository extends MongoRepository<Party, String> {
}
