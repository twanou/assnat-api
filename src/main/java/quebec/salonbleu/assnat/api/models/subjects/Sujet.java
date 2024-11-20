package quebec.salonbleu.assnat.api.models.subjects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class Sujet {

    UUID id;
    LocalDate date;
    Integer legislature;
    Integer session;
    @Schema(description = "Url menant à l'intervention sur le site web de l'Assemblée nationale.")
    String url;
    SujetDetails details;
}
