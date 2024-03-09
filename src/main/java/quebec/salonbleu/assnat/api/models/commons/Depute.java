package quebec.salonbleu.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Depute {

    UUID id;
    String titre;
    String prenom;
    String nom;
}
