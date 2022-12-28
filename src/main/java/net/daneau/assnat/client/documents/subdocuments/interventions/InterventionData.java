package net.daneau.assnat.client.documents.subdocuments.interventions;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public abstract class InterventionData {

    @Field("_class")
    private InterventionType type;
}
