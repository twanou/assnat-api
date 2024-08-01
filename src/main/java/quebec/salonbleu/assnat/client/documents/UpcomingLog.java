package quebec.salonbleu.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
@Document("upcomingLogs")
public class UpcomingLog implements UuidDocument {

    @Id
    @With
    UUID id;
    boolean loadingStatus;
    LocalDate date;
}
