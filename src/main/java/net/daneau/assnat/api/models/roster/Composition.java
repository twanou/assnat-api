package net.daneau.assnat.api.models.roster;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.Parti;

@Value
@Builder
public class Composition {

    Depute depute;
    Parti parti;
    Circonscription circonscription;
}
