package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.List;

/**
 * Dépôt de pétition
 * -Sujet
 * -Paragraphes (1er paragraphe contient nom du député(e)
 */
@RequiredArgsConstructor
public abstract class TemplateB extends Template {

    protected String DEPOT_PETITION = "Dépôt de pétition";
    private final DeputyFinder deputyFinder;

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren().stream()
                .map(subject -> {
                    Assignment assignment = this.deputyFinder.findByLastName(this.getDeputyLastName(subject.getParagraphs().getFirst()));
                    InterventionDocument interventionDocument = InterventionDocument.builder()
                            .assignmentId(assignment.getId())
                            .deputyId(assignment.getDeputyId())
                            .partyId(assignment.getPartyId())
                            .districtId(assignment.getDistrictId())
                            .paragraphs(this.baseFormat(subject.getParagraphs()))
                            .build();

                    return SubjectDetails.builder()
                            .type(this.getSubjectType())
                            .title(subject.getTitle())
                            .anchor(subject.getAnchor())
                            .interventions(List.of(interventionDocument))
                            .build();
                }).toList();
    }

    private String getDeputyLastName(String paragraph) {
        String[] splitResult = StringUtils.split(paragraph, ":", 2);
        String name = StringUtils.strip(splitResult[0]);
        return StringUtils.substringAfter(name, " ");
    }
}
