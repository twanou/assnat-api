package quebec.salonbleu.assnat.api.models.types;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SujetTypeReponse {

    List<TypeDescription> types;
}
