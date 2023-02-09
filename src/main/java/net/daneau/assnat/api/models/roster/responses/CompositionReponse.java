package net.daneau.assnat.api.models.roster.responses;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.roster.Composition;

import java.util.List;

@Value
@Builder
public class CompositionReponse {

    @Builder.Default
    List<Composition> compositions = List.of();
}
