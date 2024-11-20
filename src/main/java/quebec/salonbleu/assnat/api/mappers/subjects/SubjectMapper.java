package quebec.salonbleu.assnat.api.mappers.subjects;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.subjects.requests.SujetRequete;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {

    private final Map<SubjectType, SubjectTypeMapper> subjectMappers;
    private final AssnatLinkBuilder assnatLinkBuilder;
    private final TypeMapper typeMapper;

    public SubjectMapper(List<SubjectTypeMapper> subjectMappersList, AssnatLinkBuilder assnatLinkBuilder, TypeMapper typeMapper) {
        this.assnatLinkBuilder = assnatLinkBuilder;
        this.typeMapper = typeMapper;
        this.subjectMappers = Collections.unmodifiableMap(
                subjectMappersList.stream()
                        .flatMap(m -> m.supports().stream().map(t -> new AbstractMap.SimpleEntry<>(t, m)))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (v1, v2) -> {
                                    throw new IllegalStateException("Mapper en double.");
                                },
                                () -> new EnumMap<>(SubjectType.class))));
        if (this.subjectMappers.size() != SubjectType.values().length) {
            throw new IllegalStateException("Il manque des mappers.");
        }
    }

    public List<Sujet> toCompleteSujetsList(Iterable<Subject> subjects, Map<UUID, Affectation> affectations) {
        List<Sujet> sujets = new ArrayList<>();
        for (Subject subject : subjects) {
            Sujet sujet = Sujet.builder()
                    .id(subject.getId())
                    .date(subject.getDate())
                    .legislature(subject.getLegislature())
                    .session(subject.getSession())
                    .url(this.assnatLinkBuilder.getUrl(subject.getLegislature(), subject.getSession(), subject.getDate(), subject.getPageId(), subject.getSubjectDetails().getAnchor()))
                    .details(this.subjectMappers.get(subject.getSubjectDetails().getType()).completeMap(subject.getSubjectDetails(), affectations))
                    .build();
            sujets.add(sujet);
        }
        return Collections.unmodifiableList(sujets);
    }

    public List<Sujet> toPartialSujetsList(Iterable<Subject> subjects, Map<UUID, Affectation> affectations) {
        List<Sujet> sujets = new ArrayList<>();
        for (Subject subject : subjects) {
            Sujet sujet = Sujet.builder()
                    .id(subject.getId())
                    .date(subject.getDate())
                    .details(this.subjectMappers.get(subject.getSubjectDetails().getType()).partialMap(subject.getSubjectDetails(), affectations))
                    .build();
            sujets.add(sujet);
        }
        return Collections.unmodifiableList(sujets);
    }

    public SubjectArgs toSubjectArgs(SujetRequete sujetRequete) {
        return SubjectArgs.builder()
                .phrase(sujetRequete.getPhrase())
                .keywords(sujetRequete.getMotsCles())
                .deputyIds(sujetRequete.getDeputeIds())
                .partyIds(sujetRequete.getPartiIds())
                .districtIds(sujetRequete.getCirconscriptionIds())
                .subjectTypes(this.toSubjectTypes(sujetRequete.getSujetTypes()))
                .build();
    }

    public Set<String> toSubjectTypes(Set<SujetType> sujetTypes) {
        return sujetTypes.stream().map(t -> this.typeMapper.map(t).name()).collect(Collectors.toUnmodifiableSet());
    }
}
