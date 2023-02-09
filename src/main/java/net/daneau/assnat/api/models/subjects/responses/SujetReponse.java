package net.daneau.assnat.api.models.subjects.responses;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.subjects.Sujet;

import java.util.List;

@Value
@Builder
public class SujetReponse {

    @Builder.Default
    List<Sujet> sujets = List.of();
}
