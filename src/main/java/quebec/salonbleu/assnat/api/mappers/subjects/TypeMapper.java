package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

@Component
class TypeMapper {

    private final Map<SubjectType, SujetType> typeMap;

    TypeMapper() {
        this.typeMap = new EnumMap<>(Map.of(
                SubjectType.DEPUTY_DECLARATION, SujetType.DECLARATION_DEPUTE,
                SubjectType.QUESTIONS_ANSWERS, SujetType.QUESTIONS_REPONSES)
        );
        if (!this.typeMap.keySet().containsAll(EnumSet.allOf(SubjectType.class))) {
            throw new IllegalStateException();
        }
    }

    SujetType map(SubjectType subjectType) {
        return this.typeMap.get(subjectType);
    }
}
