package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.Deputy;

import java.util.UUID;

public interface DeputyRepository extends MongoRepository<Deputy, UUID> {

}
