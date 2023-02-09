package net.daneau.assnat.scrappers.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ScrapedDeputy {

    String firstName;
    String lastName;
    String district;
    String party;
}
