package quebec.salonbleu.assnat.api.mappers.subjects;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

@Component
public class TypeMapper {

    private final Map<SubjectType, SujetType> typeMap;

    public TypeMapper() {
        this.typeMap = new EnumMap<>(Map.of(
                SubjectType.DEPUTY_DECLARATION, SujetType.DECLARATION_DEPUTE,
                SubjectType.QUESTIONS_ANSWERS, SujetType.QUESTIONS_REPONSES,
                SubjectType.PETITION, SujetType.DEPOT_PETITION,
                SubjectType.MINISTERIAL_DECLARATION, SujetType.DECLARATION_MINISTERIELLE,
                SubjectType.LAW_PROJECT_PRESENTATION, SujetType.PRESENTATION_PROJET_LOI
        ));
        if (!this.typeMap.keySet().containsAll(EnumSet.allOf(SubjectType.class))) {
            throw new IllegalStateException();
        }
    }

    public SujetType map(SubjectType subjectType) {
        return this.typeMap.get(subjectType);
    }

    public SubjectType map(SujetType sujetType) {
        return this.typeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(sujetType))
                .findFirst()
                .orElseThrow()
                .getKey();
    }
}
