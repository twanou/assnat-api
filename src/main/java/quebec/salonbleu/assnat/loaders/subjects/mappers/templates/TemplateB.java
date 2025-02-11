package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dépôt de pétition
 * -Sujet
 * |-Paragraphes (1er paragraphe contient nom du député(e)
 */
@RequiredArgsConstructor
public abstract class TemplateB extends DocumentTypeMapper {

    private final DeputyFinder deputyFinder;
    public static final String DEPOT_PETITIONS = "Dépôt de pétitions";

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        List<SubjectDetails> subjectDetails = new ArrayList<>();
        Assignment previousAssignment = null;
        for (ScrapedLogNode subject : logNode.getChildren()) {
            Assignment assignment = this.findByLastName(this.deputyFinder, this.getDeputyLastName(subject.getParagraphs().getFirst())).orElse(previousAssignment);
            previousAssignment = assignment;
            InterventionDocument interventionDocument = this.mapAssignment(assignment, subject.getParagraphs());
            subjectDetails.add(SubjectDetails.builder()
                    .type(this.getSubjectType())
                    .title(subject.getTitle())
                    .anchor(subject.getAnchor())
                    .interventions(List.of(interventionDocument))
                    .build());
        }
        return Collections.unmodifiableList(subjectDetails);
    }
}
