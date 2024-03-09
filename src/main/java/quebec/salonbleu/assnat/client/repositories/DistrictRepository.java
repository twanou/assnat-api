package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.District;

import java.util.UUID;

public interface DistrictRepository extends MongoRepository<District, UUID> {
}
