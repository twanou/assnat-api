package quebec.salonbleu.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;


@Value
@Builder
public class InterventionDocument {

    UUID assignmentId;
    UUID deputyId;
    UUID districtId;
    UUID partyId;
    @Builder.Default
    List<String> paragraphs = List.of();
}
