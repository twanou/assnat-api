package net.daneau.assnat.api.models.commons;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class DirectoryDTO {

    @Builder.Default
    Map<String, Depute> deputies = Map.of();
    @Builder.Default
    Map<String, Parti> partis = Map.of();
    @Builder.Default
    Map<String, Circonscription> circonscriptions = Map.of();
}
