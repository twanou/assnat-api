package quebec.salonbleu.assnat.api.models.subjects.requests;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class SujetRequete {

    String phrase;

    @NotNull
    @Size(max = 25)
    @Builder.Default
    Set<String> motsCles = Set.of();

    @NotNull
    @Size(max = 125)
    @Builder.Default
    Set<UUID> deputeIds = Set.of();

    @NotNull
    @Size(max = 5)
    @Builder.Default
    Set<UUID> partiIds = Set.of();

    @NotNull
    @Size(max = 125)
    @Builder.Default
    Set<UUID> circonscriptionIds = Set.of();

    @Min(0)
    @Builder.Default
    int page = 0;

    @Min(1)
    @Max(25)
    @Builder.Default
    int taille = 25;
}

