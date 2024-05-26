package net.daneau.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Affectation {

    Depute depute;
    Parti parti;
    Circonscription circonscription;
    List<String> fonctions;
}
