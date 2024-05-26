package net.daneau.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Parti {

    String id;
    String nom;
    String sigle;
}
