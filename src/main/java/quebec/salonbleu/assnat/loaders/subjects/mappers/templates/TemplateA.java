package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.List;

/**
 * Question et réponses orales
 * -Sujet
 * --Nom du député(e)
 * --Paragraphes
 */
@RequiredArgsConstructor
public abstract class TemplateA extends DocumentTypeMapper {

    private final DeputyFinder deputyFinder;

    public static final String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";
    public static final String QUESTIONS_REPONSES = "Questions et réponses orales";
    public static final String MINISTERIAL_DECLARATION = "Déclarations ministérielles";
    private static final List<String> IGNORED_TITLES = List.of("Document déposé");

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren()
                .stream()
                .map(subject -> {
                    List<InterventionDocument> interventionDocuments = subject.getChildren()
                            .stream()
                            .filter(intervention -> !IGNORED_TITLES.contains(intervention.getTitle()))
                            .map(intervention -> {
                                Assignment assignment = this.deputyFinder.findByCompleteName(intervention.getTitle()); // nom complet, ex M. Bob Tremblay
                                return InterventionDocument.builder()
                                        .assignmentId(assignment.getId())
                                        .deputyId(assignment.getDeputyId())
                                        .partyId(assignment.getPartyId())
                                        .districtId(assignment.getDistrictId())
                                        .paragraphs(this.baseFormat(intervention.getParagraphs()))
                                        .build();
                            }).toList();

                    return SubjectDetails.builder()
                            .type(this.getSubjectType())
                            .title(subject.getTitle())
                            .anchor(subject.getAnchor())
                            .interventions(interventionDocuments)
                            .build();
                }).toList();
    }
}
