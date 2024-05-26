package net.daneau.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import net.daneau.assnat.client.documents.subdocuments.SubjectData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Value
@Builder
@Document("subjects")
public class Subject {

    @Id
    String id;
    LocalDate date;
    int legislature;
    int session;
    SubjectData subjectData;
}
