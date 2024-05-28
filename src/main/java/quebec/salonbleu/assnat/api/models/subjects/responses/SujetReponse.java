package quebec.salonbleu.assnat.api.models.subjects.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class SujetReponse {

    @Builder.Default
    List<Sujet> sujets = List.of();

    @Schema(description = "Date des derniers journaux disponibles. (Final)")
    LocalDate derniereMaj;

    @Schema(description = "Date des prochains journaux disponibles. (Préliminaire)")
    @Builder.Default
    List<LocalDate> futuresMaj = List.of();
}
