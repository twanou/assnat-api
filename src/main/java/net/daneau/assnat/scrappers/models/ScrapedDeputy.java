package net.daneau.assnat.scrappers.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ScrapedDeputy {

    String title;
    String firstName;
    String lastName;
    String district;
    String party;
    @Builder.Default
    List<String> functions = List.of();
    /**
     * Photo encodée en base64
     */
    @EqualsAndHashCode.Exclude
    String photo;
}
