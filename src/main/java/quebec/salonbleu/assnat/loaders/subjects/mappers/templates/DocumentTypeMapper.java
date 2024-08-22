package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import org.apache.commons.lang3.StringUtils;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.subdocuments.InterventionDocument;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.util.List;
import java.util.stream.IntStream;

public abstract class DocumentTypeMapper {

    public static final String AFFAIRES_COURANTES = "Affaires courantes";

    public abstract List<SubjectDetails> map(ScrapedLogNode logNode);

    public abstract List<String> supports();

    protected abstract List<String> format(List<String> paragraphs);

    protected abstract SubjectType getSubjectType();

    protected List<String> baseFormat(List<String> paragraphs) {
        List<String> formattedParagraphs = IntStream.range(0, paragraphs.size())
                .mapToObj(i -> i == 0 ? this.removeDeputyName(paragraphs.get(i)) : paragraphs.get(i))
                .filter(s -> !(StringUtils.startsWith(s, "•") && StringUtils.endsWith(s, "•")))
                .toList();
        return this.format(formattedParagraphs);
    }

    protected InterventionDocument mapAssignment(Assignment assignment, List<String> paragraphs) {
        return InterventionDocument.builder()
                .assignmentId(assignment.getId())
                .deputyId(assignment.getDeputyId())
                .partyId(assignment.getPartyId())
                .districtId(assignment.getDistrictId())
                .paragraphs(this.baseFormat(paragraphs))
                .build();
    }

    protected InterventionDocument mapParagraphs(List<String> paragraphs) {
        return InterventionDocument.builder()
                .paragraphs(this.baseFormat(paragraphs))
                .note(this.getDeputyLastName(paragraphs.getFirst()))
                .build();
    }

    protected InterventionDocument mapLogNode(ScrapedLogNode scrapedLogNode) {
        return InterventionDocument.builder()
                .paragraphs(scrapedLogNode.getParagraphs())
                .note(scrapedLogNode.getTitle())
                .build();
    }

    protected String getDeputyLastName(String paragraph) {
        String[] splitResult = StringUtils.split(paragraph, ":", 2);
        return StringUtils.strip(splitResult[0]);
    }

    private String removeDeputyName(String paragraph) {
        String[] splitResult = StringUtils.split(paragraph, ":", 2);
        return splitResult.length == 2 ? StringUtils.strip(splitResult[1]) : paragraph;
    }
}
