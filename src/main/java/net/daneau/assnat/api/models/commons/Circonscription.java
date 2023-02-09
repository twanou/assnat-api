package net.daneau.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
@Builder
public class Circonscription {

    @Id
    String id;
    String nom;
}
