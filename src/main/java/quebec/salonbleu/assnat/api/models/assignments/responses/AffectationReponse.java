package quebec.salonbleu.assnat.api.models.assignments.responses;

import lombok.Builder;
import lombok.Value;
import quebec.salonbleu.assnat.api.models.commons.Affectation;

import java.util.Collection;
import java.util.List;

@Value
@Builder
public class AffectationReponse {

    @Builder.Default
    Collection<Affectation> affectations = List.of();
}
