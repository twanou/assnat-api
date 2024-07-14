package quebec.salonbleu.assnat.api.models.types;

import lombok.AllArgsConstructor;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;

@Value
@AllArgsConstructor(staticName = "of")
public class TypeDescription {

    SujetType type;
    String description;
}
