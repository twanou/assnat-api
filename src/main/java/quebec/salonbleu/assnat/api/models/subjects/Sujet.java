package quebec.salonbleu.assnat.api.models.subjects;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Sujet {
    String id;
    LocalDate date;
    int legislature;
    int session;
    String url;
    SujetDetails details;
}