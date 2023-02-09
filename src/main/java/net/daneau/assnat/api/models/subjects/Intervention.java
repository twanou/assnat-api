package net.daneau.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.Parti;

import java.util.List;

@Value
@Builder
public class Intervention {

    Depute depute;
    Circonscription circonscription;
    Parti parti;
    @Builder.Default
    List<String> paragraphes = List.of();
}
