package quebec.salonbleu.assnat.client.repositories.args;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SubjectArgs {

    String searchString;
    @Builder.Default
    Set<UUID> deputyIds = Set.of();
    @Builder.Default
    Set<UUID> partyIds = Set.of();
    @Builder.Default
    Set<UUID> districtIds = Set.of();

    public Optional<String> getSearchString() {
        return Optional.ofNullable(searchString);
    }
}
