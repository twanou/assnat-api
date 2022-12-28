package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@TypeAlias("DEPUTY")
@Document("deputies")
public class Deputy {

    @Id
    String id;
    String firstName;
    String lastName;
    String title;
}
