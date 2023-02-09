package net.daneau.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SubjectDetails {
    SubjectType type;
    String title;
    @Builder.Default
    List<InterventionDocument> interventions = List.of();
}
