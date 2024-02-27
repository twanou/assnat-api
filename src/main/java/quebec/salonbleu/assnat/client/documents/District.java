package quebec.salonbleu.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@Document("districts")
public class District {

    @Id
    String id;
    String name;
}
