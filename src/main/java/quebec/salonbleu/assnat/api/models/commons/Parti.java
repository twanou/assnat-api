package quebec.salonbleu.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Parti {

    UUID id;
    String nom;
    String sigle;
}
