package quebec.salonbleu.assnat.loaders.subjects.mappers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public abstract class SubjectDocumentTypeMapper {

    protected String AFFAIRES_COURANTES = "Affaires courantes";
    protected String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";
    protected String QUESTIONS_REPONSES = "Questions et réponses orales";
    private final List<String> IGNORED_TITLES = List.of("Document déposé");
    private final DeputyFinder deputyFinder;

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

    public abstract List<String> supports();

    protected abstract List<String> format(List<String> paragraphs);

    protected abstract SubjectType getSubjectType();

    private List<String> baseFormat(List<String> paragraphs) {
        List<String> formattedParagraphs = IntStream.range(0, paragraphs.size())
                .mapToObj(i -> i == 0 ? this.removeDeputyName(paragraphs.get(i)) : paragraphs.get(i))
                .filter(s -> !(StringUtils.startsWith(s, "•") && StringUtils.endsWith(s, "•")))
                .toList();
        return this.format(formattedParagraphs);
    }

    private String removeDeputyName(String paragraph) {
        String[] splitResult = paragraph.split(":");
        return splitResult.length == 2 ? StringUtils.strip(splitResult[1]) : paragraph;
    }
}
