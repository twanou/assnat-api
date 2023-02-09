package net.daneau.assnat.client.repositories;

import net.daneau.assnat.client.documents.District;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DistrictRepository extends MongoRepository<District, String> {
}
