package net.daneau.assnat.loaders.subjects.mappers;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionsAnswersDocumentMapper implements SubjectDocumentTypeMapper {

    private final DeputyFinder deputyFinder;

    @Override
    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren()
                .stream()
                .map(qaNode -> {
                    List<InterventionDocument> interventionDocuments = qaNode.getChildren()
                            .stream()
                            .map(intervention -> {
                                Assignment assignment = this.deputyFinder.findByCompleteName(intervention.getTitle()); // nom complet, ex M. Bob Tremblay
                                return InterventionDocument.builder()
                                        .deputyId(assignment.getDeputyId())
                                        .partyId(assignment.getPartyId())
                                        .districtId(assignment.getDistrictId())
                                        .paragraphs(intervention.getParagraphs())
                                        .build();
                            }).toList();

                    return SubjectDetails.builder()
                            .type(SubjectType.QUESTIONS_ANSWERS)
                            .title(qaNode.getTitle())
                            .interventions(interventionDocuments)
                            .build();
                }).toList();
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, QUESTIONS_REPONSES);
    }
}

