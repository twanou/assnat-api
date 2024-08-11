package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA;

import java.util.List;

@Component
public class LawProjectPresentationDocumentMapper extends TemplateA {

    public LawProjectPresentationDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> format(List<String> paragraphs) {
        return paragraphs;
    }

    @Override
    public SubjectType getSubjectType() {
        return SubjectType.LAW_PROJECT_PRESENTATION;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, LAW_PROJECT_PRESENTATION);
    }
}
