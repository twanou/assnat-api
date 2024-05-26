package quebec.salonbleu.assnat.api.models.deputies;

import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.commons.Depute;

import java.util.List;

@Value
@Builder
public class DeputeReponse {

    @Builder.Default
    List<Depute> deputes = List.of();
}
