package quebec.salonbleu.assnat.client.documents.subdocuments;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Value
@Builder
public class InterventionDocument {

    @Nullable
    UUID assignmentId;
    @Nullable
    UUID deputyId;
    @Nullable
    UUID districtId;
    @Nullable
    UUID partyId;
    String note;
    @Builder.Default
    List<String> paragraphs = List.of();

    public Optional<UUID> getOptionalAssignmentId() {
        return Optional.ofNullable(assignmentId);
    }
}
