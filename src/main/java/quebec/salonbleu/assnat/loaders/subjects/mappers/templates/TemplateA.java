package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.List;
import java.util.Optional;

/**
 * Catégorie
 * -Sujet
 * --Nom du député(e)
 * |--Paragraphes
 * --Mise aux voix (optionnel)
 */
@RequiredArgsConstructor
public abstract class TemplateA extends DocumentTypeMapper {

    private final DeputyFinder deputyFinder;

    public static final String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";
    public static final String QUESTIONS_REPONSES = "Questions et réponses orales";
    public static final String MINISTERIAL_DECLARATION = "Déclarations ministérielles";
    public static final String LAW_PROJECT_PRESENTATION = "Présentation de projets de loi";
    private static final List<String> IGNORED_TITLES = List.of("Document déposé");
    private static final String MISE_AUX_VOIX = "Mise aux voix";

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren()
                .stream()
                .map(subject -> {
                    List<InterventionDocument> interventionDocuments = subject.getChildren()
                            .stream()
                            .filter(intervention -> !IGNORED_TITLES.contains(intervention.getTitle()))
                            .map(intervention -> this.getAssignment(intervention)
                                    .map(a -> this.mapAssignment(a, intervention.getParagraphs()))
                                    .orElse(this.mapLogNode(intervention)))
                            .toList();

                    return SubjectDetails.builder()
                            .type(this.getSubjectType())
                            .title(subject.getTitle())
                            .anchor(subject.getAnchor())
                            .interventions(interventionDocuments)
                            .build();
                }).toList();
    }

    private Optional<Assignment> getAssignment(ScrapedLogNode intervention) {
        String deputyName = this.getDeputyLastName(intervention.getParagraphs().getFirst());
        if (Strings.CI.contains(deputyName, "(")) {
            return this.findByLastName(deputyFinder, deputyName);
        }
        if (!MISE_AUX_VOIX.equals(intervention.getTitle())) {
            return this.deputyFinder.findByCompleteName(intervention.getTitle()); // nom complet, ex M. Bob Tremblay
        }
        return Optional.empty();
    }
}
