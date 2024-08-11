package quebec.salonbleu.assnat.api.mappers.subjects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.Intervention;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GenericSubjectTypeMapper implements SubjectTypeMapper {

    private final TypeMapper typeMapper;

    @Override
    public SujetDetails map(SubjectDetails subjectDetails, Map<UUID, Affectation> affectations) {
        return SujetDetails.builder()
                .titre(subjectDetails.getTitle())
                .type(this.typeMapper.map(subjectDetails.getType()))
                .interventions(Collections.unmodifiableList(this.mapInterventions(subjectDetails.getInterventions(), affectations)))
                .build();
    }

    @Override
    public EnumSet<SubjectType> supports() {
        return EnumSet.allOf(SubjectType.class);
    }

    private List<Intervention> mapInterventions(List<InterventionDocument> interventionDocuments, Map<UUID, Affectation> affectations) {
        return interventionDocuments.stream()
                .map(interventionDocument -> {
                    Intervention.InterventionBuilder interventionBuilder = Intervention.builder();
                    interventionDocument.getOptionalAssignmentId().ifPresentOrElse(
                            i -> interventionBuilder
                                    .affectation(affectations.get(interventionDocument.getAssignmentId()))
                                    .paragraphes(Collections.unmodifiableList(interventionDocument.getParagraphs())),
                            () -> interventionBuilder
                                    .note(interventionDocument.getNote())
                                    .paragraphes(Collections.unmodifiableList(interventionDocument.getParagraphs())));
                    return interventionBuilder.build();
                })
                .toList();
    }
}

