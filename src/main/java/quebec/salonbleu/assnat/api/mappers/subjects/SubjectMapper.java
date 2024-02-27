package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {

    private final Map<SubjectType, SubjectTypeMapper> subjectMappers;
    private final AssnatLinkBuilder assnatLinkBuilder;

    public SubjectMapper(List<SubjectTypeMapper> subjectMappersList, AssnatLinkBuilder assnatLinkBuilder) {
        this.assnatLinkBuilder = assnatLinkBuilder;
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

    public List<Sujet> toSujetsList(Iterable<Subject> subjects, Map<String, Affectation> affectations) {
        List<Sujet> sujets = new ArrayList<>();
        for (Subject subject : subjects) {
            Sujet sujet = Sujet.builder()
                    .id(subject.getId())
                    .date(subject.getDate())
                    .legislature(subject.getLegislature())
                    .session(subject.getSession())
                    .url(this.assnatLinkBuilder.getUrl(subject.getLegislature(), subject.getSession(), subject.getDate(), subject.getPageId(), subject.getSubjectDetails().getAnchor()))
                    .details(this.subjectMappers.get(subject.getSubjectDetails().getType()).map(subject.getSubjectDetails(), affectations))
                    .build();
            sujets.add(sujet);
        }
        return sujets;
    }
}
