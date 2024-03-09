package quebec.salonbleu.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class Sujet {
    
    UUID id;
    LocalDate date;
    int legislature;
    int session;
    String url;
    SujetDetails details;
}
