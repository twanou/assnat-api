package quebec.salonbleu.assnat.api.models.subjects.responses;

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
    LocalDate derniereMaj;
}
