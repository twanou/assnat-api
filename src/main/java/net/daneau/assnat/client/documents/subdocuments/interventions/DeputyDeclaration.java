package net.daneau.assnat.client.documents.subdocuments.interventions;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Value
@Builder
@Document
@EqualsAndHashCode(callSuper = false)
@TypeAlias(InterventionType.Constants.DEPUTY_DECLARATION)
public class DeputyDeclaration extends InterventionData {

    String title;
    @Builder.Default
    List<String> paragraphs = List.of();
}
