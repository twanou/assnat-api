package quebec.salonbleu.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Circonscription {

    String id;
    String nom;
}
