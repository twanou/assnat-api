package quebec.salonbleu.assnat.api.models.districts;

import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;

import java.util.List;

@Value
@Builder
public class CirconscriptionReponse {

    @Builder.Default
    List<Circonscription> circonscriptions = List.of();
}
