package net.daneau.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SubjectData {
    SubjectDataType type;
    String title;
    @Builder.Default
    List<Intervention> interventions = List.of();
}
