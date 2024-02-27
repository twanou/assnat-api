package quebec.salonbleu.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Depute {

    String id;
    String titre;
    String prenom;
    String nom;
}
