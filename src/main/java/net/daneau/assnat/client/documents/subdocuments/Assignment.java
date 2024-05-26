package net.daneau.assnat.client.documents.subdocuments;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Value
@Builder
public class Assignment {

    @Field(targetType = FieldType.OBJECT_ID)
    String deputyId;
    @Field(targetType = FieldType.OBJECT_ID)
    String districtId;
    @Field(targetType = FieldType.OBJECT_ID)
    String partyId;
}
