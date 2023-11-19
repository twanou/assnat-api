package net.daneau.assnat.loaders.subjects.mappers;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;

import java.util.List;

@RequiredArgsConstructor
public abstract class SubjectDocumentTypeMapper {

    protected String AFFAIRES_COURANTES = "Affaires courantes";
    protected String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";
    protected String QUESTIONS_REPONSES = "Questions et réponses orales";
    private final DeputyFinder deputyFinder;

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren()
                .stream()
                .map(subject -> {
                    List<InterventionDocument> interventionDocuments = subject.getChildren()
                            .stream()
                            .map(intervention -> {
                                Assignment assignment = this.deputyFinder.findByCompleteName(intervention.getTitle()); // nom complet, ex M. Bob Tremblay
                                return InterventionDocument.builder()
                                        .deputyId(assignment.getDeputyId())
                                        .partyId(assignment.getPartyId())
                                        .districtId(assignment.getDistrictId())
                                        .paragraphs(this.format(intervention.getParagraphs()))
                                        .build();
                            }).toList();

                    return SubjectDetails.builder()
                            .type(this.getSubjectType())
                            .title(subject.getTitle())
                            .interventions(interventionDocuments)
                            .build();
                }).toList();
    }

    public abstract List<String> supports();

    protected abstract List<String> format(List<String> paragraphs);

    protected abstract SubjectType getSubjectType();


}
