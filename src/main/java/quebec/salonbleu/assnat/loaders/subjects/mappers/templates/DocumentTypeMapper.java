package quebec.salonbleu.assnat.loaders.subjects.mappers.templates;

import org.apache.commons.lang3.StringUtils;
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

    private String removeDeputyName(String paragraph) {
        String[] splitResult = StringUtils.split(paragraph, ":", 2);
        return splitResult.length == 2 ? StringUtils.strip(splitResult[1]) : paragraph;
    }
}
