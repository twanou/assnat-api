package quebec.salonbleu.assnat.api.models.parties;

import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.commons.Parti;

import java.util.List;

@Value
@Builder
public class PartiReponse {

    @Builder.Default
    List<Parti> partis = List.of();
}
