package quebec.salonbleu.assnat.client.repositories.args;

import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SubjectArgs {

    String phrase;
    @Builder.Default
    Set<String> keywords = Set.of();
    @Builder.Default
    Set<UUID> deputyIds = Set.of();
    @Builder.Default
    Set<UUID> partyIds = Set.of();
    @Builder.Default
    Set<UUID> districtIds = Set.of();
    @Builder.Default
    Set<String> subjectTypes = Set.of();
}
