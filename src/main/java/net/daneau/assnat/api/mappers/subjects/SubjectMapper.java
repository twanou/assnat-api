package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
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

    public SubjectMapper(List<SubjectTypeMapper> subjectMappersList) {
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

    public List<Sujet> toSujetsList(Iterable<Subject> subjects, DirectoryDTO directoryDTO) {
        List<Sujet> sujets = new ArrayList<>();
        for (Subject subject : subjects) {
            Sujet sujet = Sujet.builder()
                    .date(subject.getDate())
                    .legislature(subject.getLegislature())
                    .session(subject.getSession())
                    .details(this.subjectMappers.get(subject.getSubjectDetails().getType()).map(subject.getSubjectDetails(), directoryDTO))
                    .build();
            sujets.add(sujet);
        }
        return sujets;
    }
}
