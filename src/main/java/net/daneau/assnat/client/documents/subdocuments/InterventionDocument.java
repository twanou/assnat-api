package net.daneau.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;


@Value
@Builder
public class InterventionDocument {

    @Field(targetType = FieldType.OBJECT_ID)
    String deputyId;
    @Field(targetType = FieldType.OBJECT_ID)
    String districtId;
    @Field(targetType = FieldType.OBJECT_ID)
    String partyId;
    @Builder.Default
    List<String> paragraphs = List.of();
}
