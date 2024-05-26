package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.client.documents.subdocuments.interventions.InterventionData;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Value
@Builder
@TypeAlias("INTERVENTION")
@Document("interventions")
public class Intervention {

    @Id
    String id;
    LocalDate date;
    int legislature;
    int session;
    String deputyId;
    String ridingId;
    String partyId;
    String groupId;
    int sequence;
    InterventionData interventionData;
}
