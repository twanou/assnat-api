package net.daneau.assnat.api.mappers.subjects;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.subjects.Intervention;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenericSubjectTypeMapper implements SubjectTypeMapper {

    private final TypeMapper typeMapper;

    @Override
    public SujetDetails map(SubjectDetails subjectDetails, DirectoryDTO directoryDTO) {
        return SujetDetails.builder()
                .titre(subjectDetails.getTitle())
                .type(this.typeMapper.map(subjectDetails.getType()))
                .interventions(Collections.unmodifiableList(this.mapInterventions(subjectDetails.getInterventions(), directoryDTO)))
                .build();
    }

    @Override
    public EnumSet<SubjectType> supports() {
        return EnumSet.of(SubjectType.DEPUTY_DECLARATION, SubjectType.QUESTIONS_ANSWERS);
    }

    private List<Intervention> mapInterventions(List<InterventionDocument> interventionDocuments, DirectoryDTO directoryDTO) {
        return interventionDocuments.stream()
                .map(i -> Intervention.builder()
                        .parti(directoryDTO.getPartis().get(i.getPartyId()))
                        .depute(directoryDTO.getDeputies().get(i.getDeputyId()))
                        .circonscription(directoryDTO.getCirconscriptions().get(i.getDistrictId()))
                        .paragraphes(Collections.unmodifiableList(i.getParagraphs()))
                        .build())
                .toList();
    }
}
