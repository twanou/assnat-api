package quebec.salonbleu.assnat.client.repositories;

import quebec.salonbleu.assnat.client.documents.Deputy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeputyRepository extends MongoRepository<Deputy, String> {

}
