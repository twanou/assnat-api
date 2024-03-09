package quebec.salonbleu.assnat.client.documents;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;

import java.time.LocalDate;
import java.util.UUID;


@Value
@Builder
@Document("subjects")
public class Subject implements UuidDocument {

    @Id
    @With
    UUID id;
    String pageId;
    LocalDate date;
    int legislature;
    int session;
    SubjectDetails subjectDetails;
}
