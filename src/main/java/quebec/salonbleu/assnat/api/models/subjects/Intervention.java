package quebec.salonbleu.assnat.api.models.subjects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.commons.Affectation;

import java.util.List;

@Value
@Builder
public class Intervention {

    @Schema(description = "Identification du député(e) faisant l'intervention.", nullable = true)
    Affectation affectation;
    @Schema(description = "Note donnant plus de détail sur la nature de l'intervention si aucune identification disponible.", nullable = true)
    String note;
    @Builder.Default
    List<String> paragraphes = List.of();
}
