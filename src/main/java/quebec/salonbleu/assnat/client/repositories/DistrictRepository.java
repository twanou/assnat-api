package quebec.salonbleu.assnat.client.repositories;

import quebec.salonbleu.assnat.client.documents.District;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DistrictRepository extends MongoRepository<District, String> {
}
