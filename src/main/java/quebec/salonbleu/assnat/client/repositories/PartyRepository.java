package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.Party;

import java.util.UUID;

public interface PartyRepository extends MongoRepository<Party, UUID> {
}
