package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Document("assignments")
public class Assignment {

    @Id
    String id;
    int hash;
    LocalDate startDate;
    @With
    LocalDate endDate;
    @Field(targetType = FieldType.OBJECT_ID)
    String deputyId;
    @Field(targetType = FieldType.OBJECT_ID)
    String districtId;
    @Field(targetType = FieldType.OBJECT_ID)
    String partyId;
    String photo;
    @Builder.Default
    List<String> functions = List.of();
}
