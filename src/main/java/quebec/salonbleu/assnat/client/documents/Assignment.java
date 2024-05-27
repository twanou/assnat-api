package quebec.salonbleu.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
@Document("assignments")
public class Assignment implements UuidDocument {

    @Id
    @With
    UUID id;
    int hash;
    LocalDate startDate;
    @With
    LocalDate endDate;
    UUID deputyId;
    UUID districtId;
    UUID partyId;
    @Builder.Default
    List<String> functions = List.of();
}
