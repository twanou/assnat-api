package quebec.salonbleu.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SujetDetails {

    SujetType type;
    String titre;
    @Builder.Default
    List<Intervention> interventions = List.of();
}
