package quebec.salonbleu.assnat.api.models.errors;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErreurReponse {

    String code;
    String message;
}
