package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Value
@Builder
@Document("deputies")
public class Deputy {

    @Id
    String id;
    String firstName;
    String lastName;
    String title;
    @Builder.Default
    List<String> functions = List.of();
}
