package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Catégorie
 * -Sujet
 * |-Paragraphes (1er paragraphe contient nom du député(e)
 * --Autres député(e)s
 * |--Paragraphes (1er paragraphe contient nom du député(e)
 * --Mise aux voix
 */
@RequiredArgsConstructor
public abstract class TemplateC extends DocumentTypeMapper {

    private final DeputyFinder deputyFinder;
    public static final String MOTIONS_SANS_PREAVIS = "Motions sans préavis";
    private static final String MISE_AUX_VOIX = "Mise aux voix";
    private static final List<String> PRESIDENT = List.of("La Présidente", "Le Président");
    private static final List<String> IGNORED_TITLES = List.of("Document déposé");

    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        return logNode.getChildren().stream()
                .flatMap(subject -> {
                    List<SubjectDetails> subjectDetails = new ArrayList<>();
                    //Parfois il faut descendre d'un niveau car il y a un document déposé
                    List<String> paragraphs = subject.getParagraphs().isEmpty() ? subject.getChildren().getFirst().getParagraphs() : subject.getParagraphs();
                    Optional<Assignment> assignment = this.getAssignment(paragraphs.getFirst());
                    InterventionDocument interventionDocument = assignment.map(a -> this.mapAssignment(a, paragraphs)).orElse(this.mapParagraphs(paragraphs));
                    List<InterventionDocument> interventionDocuments = subject.getChildren()
                            .stream()
                            .filter(intervention -> !IGNORED_TITLES.contains(intervention.getTitle()))
                            .map(subSubject -> {
                                List<SubjectDetails> rejectedMotions = List.of();
                                if (MISE_AUX_VOIX.equals(subSubject.getTitle())) {
                                    rejectedMotions = this.getRejectedMotion(subSubject);
                                    subjectDetails.addAll(rejectedMotions);
                                }
                                Optional<Assignment> subAssignment = this.getSubAssignment(subSubject);
                                return subAssignment.map(a -> this.mapAssignment(a, subSubject.getParagraphs())).orElse(this.mapParagraphs(subSubject.getTitle(), subSubject.getParagraphs(), rejectedMotions));
                            }).collect(Collectors.toList());

                    interventionDocuments.addFirst(interventionDocument);
                    subjectDetails.addFirst(SubjectDetails.builder()
                            .type(this.getSubjectType())
                            .title(subject.getTitle())
                            .anchor(subject.getAnchor())
                            .interventions(interventionDocuments)
                            .build());

                    return subjectDetails.stream();
                }).toList();
    }

    private List<SubjectDetails> getRejectedMotion(ScrapedLogNode scrapedLogNode) {
        List<SubjectDetails> subjectDetails = new ArrayList<>();
        String fullText = StringUtils.join(scrapedLogNode.getParagraphs(), "###");
        String[] motions = StringUtils.substringsBetween(fullText, "«", "»");
        if (motions != null) {
            for (String motion : motions) {
                String fullParagraphsBeforeMotion = StringUtils.substringBetween(fullText, "###", "###«" + motion);
                String[] paragraphsBeforeMotion = StringUtils.split(fullParagraphsBeforeMotion, "###");

                Optional<Assignment> assignment = Optional.empty();
                int index = paragraphsBeforeMotion.length - 1;
                while (assignment.isEmpty() && index >= 0) {
                    assignment = this.getAssignment(paragraphsBeforeMotion[index]);
                    --index;
                }

                subjectDetails.add(
                        SubjectDetails.builder()
                                .type(SubjectType.MOTION_WITHOUT_NOTICE)
                                .anchor(scrapedLogNode.getAnchor())
                                .title("Motion rejetée")
                                .interventions(List.of(this.mapAssignment(assignment.orElseThrow(), Arrays.asList(StringUtils.split("«" + motion + "»", "###")))))
                                .build());
            }
        }
        return Collections.unmodifiableList(subjectDetails);
    }

    private Optional<Assignment> getAssignment(String paragraph) {
        String lastName = this.getDeputyLastName(paragraph);
        if (!PRESIDENT.contains(lastName)) {
            return this.findByLastName(lastName);
        }
        return Optional.empty();
    }

    private Optional<Assignment> getSubAssignment(ScrapedLogNode scrapedLogNode) {
        if (!MISE_AUX_VOIX.equals(scrapedLogNode.getTitle()) && !PRESIDENT.contains(scrapedLogNode.getTitle())) {
            return this.findByLastName(this.getDeputyLastName(scrapedLogNode.getParagraphs().getFirst()));
        }
        return Optional.empty();
    }

    private Optional<Assignment> findByLastName(String paragraphPrefix) {
        String district = StringUtils.substringBetween(paragraphPrefix, "(", ")");
        if (district != null) {
            String lastName = StringUtils.strip(StringUtils.substringBefore(paragraphPrefix, "("));
            return this.deputyFinder.findByLastNameAndDistrict(lastName, district);
        } else {
            return this.deputyFinder.findByLastName(this.getDeputyLastName(paragraphPrefix));
        }
    }

    private InterventionDocument mapParagraphs(String title, List<String> paragraphs, List<SubjectDetails> rejectedMotions) {
        return InterventionDocument.builder()
                .paragraphs(rejectedMotions.isEmpty() ? paragraphs : this.cleanParagraphs(paragraphs, rejectedMotions))
                .note(title)
                .build();
    }

    private List<String> cleanParagraphs(List<String> paragraphs, List<SubjectDetails> rejectedMotions) {
        int indexToDeleteFrom = paragraphs.indexOf(rejectedMotions.getFirst().getInterventions().getFirst().getParagraphs().getFirst());
        return Collections.unmodifiableList(paragraphs.subList(0, indexToDeleteFrom - 1)); //-1 pour ignorer aussi le député qui présente la motion.
    }
}
