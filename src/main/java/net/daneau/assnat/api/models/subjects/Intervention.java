package net.daneau.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.commons.Affectation;

import java.util.List;

@Value
@Builder
public class Intervention {

    Affectation affectation;
    @Builder.Default
    List<String> paragraphes = List.of();
}
