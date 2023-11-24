package net.daneau.assnat.scrappers.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.htmlunit.html.HtmlImage;

@Value
@Builder
@Jacksonized
public class ScrapedDeputy {

    String firstName;
    String lastName;
    String district;
    String party;
    @EqualsAndHashCode.Exclude
    HtmlImage image;
}
