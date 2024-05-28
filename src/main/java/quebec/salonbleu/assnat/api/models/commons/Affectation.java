package quebec.salonbleu.assnat.api.models.commons;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Affectation {

    Depute depute;
    Parti parti;
    Circonscription circonscription;
    @Schema(description = "Url de la photo du député provenant du site de l'Assemblée Nationale.")
    String photoUrl;
    List<String> fonctions;
}
