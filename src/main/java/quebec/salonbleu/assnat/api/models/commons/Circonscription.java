package quebec.salonbleu.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Circonscription {

    UUID id;
    String nom;
}
