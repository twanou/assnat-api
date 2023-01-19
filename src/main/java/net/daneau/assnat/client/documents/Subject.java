package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.client.documents.subdocuments.Intervention;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@TypeAlias("SUBJECT")
@Document("subjects")
public class Subject {

    @Id
    String id;
    SubjectType type;
    String title;
    LocalDate date;
    int legislature;
    int session;
    @Builder.Default
    List<Intervention> interventions = List.of();
}
