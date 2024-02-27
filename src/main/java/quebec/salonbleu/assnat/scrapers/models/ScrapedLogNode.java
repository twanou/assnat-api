package quebec.salonbleu.assnat.scrapers.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ScrapedLogNode {

    String title;
    String anchor;
    @Builder.Default
    List<String> paragraphs = List.of();
    @Builder.Default
    List<ScrapedLogNode> children = List.of();
}
