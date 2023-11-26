package net.daneau.assnat.scrappers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.htmlunit.html.HtmlImage;

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
    List<String> functions;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    HtmlImage image;
}
