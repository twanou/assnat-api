package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Document("rosters")
public class Roster {

    @Id
    String id;
    int hash;
    LocalDate startDate;
    @With
    LocalDate endDate;
    @Builder.Default
    public List<Assignment> assignments = List.of();
}
