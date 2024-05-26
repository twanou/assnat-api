package net.daneau.assnat.api.mappers.subjects;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.subjects.Intervention;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GenericSubjectTypeMapper implements SubjectTypeMapper {

    private final TypeMapper typeMapper;

    @Override
    public SujetDetails map(SubjectDetails subjectDetails, Map<String, Affectation> affectations) {
        return SujetDetails.builder()
                .titre(subjectDetails.getTitle())
                .type(this.typeMapper.map(subjectDetails.getType()))
                .interventions(Collections.unmodifiableList(this.mapInterventions(subjectDetails.getInterventions(), affectations)))
                .build();
    }

    @Override
    public EnumSet<SubjectType> supports() {
        return EnumSet.of(SubjectType.DEPUTY_DECLARATION, SubjectType.QUESTIONS_ANSWERS);
    }

    private List<Intervention> mapInterventions(List<InterventionDocument> interventionDocuments, Map<String, Affectation> affectations) {
        return interventionDocuments.stream()
                .map(intervention -> Intervention.builder()
                        .affectation(affectations.get(intervention.getAssignmentId()))
                        .paragraphes(Collections.unmodifiableList(intervention.getParagraphs()))
                        .build())
                .toList();
    }
}
