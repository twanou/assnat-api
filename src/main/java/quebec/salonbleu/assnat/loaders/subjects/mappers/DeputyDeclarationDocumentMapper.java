package quebec.salonbleu.assnat.loaders.subjects.mappers;

import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class DeputyDeclarationDocumentMapper extends SubjectDocumentTypeMapper {

    private static final String VICE_PRESIDENT = "vice-président";

    public DeputyDeclarationDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, DECLARATIONS_DE_DEPUTES);
    }

    @Override
    protected List<String> format(List<String> paragraphs) {
        return IntStream.range(0, this.getEndRange(paragraphs)) // On ignore à partir du moment que le VP se met à parler
                .mapToObj(i -> i == 0 ? this.removeDeputyName(paragraphs.get(i)) : paragraphs.get(i)) // retrait du nom du député au début du premier paragraphe
                .toList();
    }

    @Override
    protected SubjectType getSubjectType() {
        return SubjectType.DEPUTY_DECLARATION;
    }

    private int getEndRange(List<String> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            if (StringUtils.containsIgnoreCase(paragraphs.get(i), VICE_PRESIDENT)) {
                return i;
            }
        }
        return paragraphs.size();
    }

    private String removeDeputyName(String paragraph) {
        return StringUtils.strip(paragraph.split(":")[1]);
    }
}
