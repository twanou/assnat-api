package quebec.salonbleu.assnat.client.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;

import java.util.List;
import java.util.UUID;

public interface UpcomingLogRepository extends MongoRepository<UpcomingLog, UUID> {

    List<UpcomingLog> findAllByLoadingStatus(boolean status, Sort sort);
    
    @Query(value = "{'loadingStatus': ?0}", delete = true)
    void deleteAllByLoadingStatus(boolean status);
}
