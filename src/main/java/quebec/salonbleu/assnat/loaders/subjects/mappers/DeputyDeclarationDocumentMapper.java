package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class DeputyDeclarationDocumentMapper extends TemplateA {

    private static final String VICE_PRESIDENT = "vice-président";

    public DeputyDeclarationDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, DECLARATIONS_DE_DEPUTES);
    }

    @Override
    public List<String> format(List<String> paragraphs) {
        return IntStream.range(0, this.getEndRange(paragraphs)) // On ignore à partir du moment que le VP se met à parler
                .mapToObj(paragraphs::get)
                .toList();
    }

    @Override
    public SubjectType getSubjectType() {
        return SubjectType.DEPUTY_DECLARATION;
    }

    private int getEndRange(List<String> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            if (Strings.CI.contains(paragraphs.get(i), VICE_PRESIDENT)) {
                return i;
            }
        }
        return paragraphs.size();
    }
}
