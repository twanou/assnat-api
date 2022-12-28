package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@TypeAlias("PARTY")
@Document("parties")
public class Party {

    @Id
    String id;
    String name;
}
