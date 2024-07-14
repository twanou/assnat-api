package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.types.TypeDescription;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SubjectTypeService {

    private final MessageSource messageSource;

    public List<TypeDescription> getSubjectTypes() {
        return Arrays.stream(SujetType.values())
                .map(type -> TypeDescription.of(type, this.messageSource.getMessage(type.name(), null, Locale.CANADA_FRENCH)))
                .toList();
    }
}
