package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA;

import java.util.List;

@Component
public class MinisterialDeclarationDocumentMapper extends TemplateA {

    public MinisterialDeclarationDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> format(List<String> paragraphs) {
        return paragraphs;
    }

    @Override
    public SubjectType getSubjectType() {
        return SubjectType.MINISTERIAL_DECLARATION;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, MINISTERIAL_DECLARATION);
    }
}
