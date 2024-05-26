package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;

import java.util.UUID;

public interface UpcomingLogRepository extends MongoRepository<UpcomingLog, UUID> {
}
