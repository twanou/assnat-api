package quebec.salonbleu.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Value
@Builder
@Document("deputies")
public class Deputy implements UuidDocument {

    @Id
    @With
    UUID id;
    String firstName;
    String lastName;
    String title;
    String lastDistrict;
    @With
    String photo;
}
