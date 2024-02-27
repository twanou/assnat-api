package quebec.salonbleu.assnat.client.repositories;

import quebec.salonbleu.assnat.client.documents.Party;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyRepository extends MongoRepository<Party, String> {
}
