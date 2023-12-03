package net.daneau.assnat.api.models.assignments.responses;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.commons.Affectation;

import java.util.Collection;
import java.util.List;

@Value
@Builder
public class AffectationReponse {

    @Builder.Default
    Collection<Affectation> affectations = List.of();
}
