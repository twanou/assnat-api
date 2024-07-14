package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateB;

import java.util.List;

@Component
public class PetitionDocumentMapper extends TemplateB {

    public PetitionDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> format(List<String> paragraphs) {
        return paragraphs;
    }

    @Override
    public SubjectType getSubjectType() {
        return SubjectType.PETITION;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, DEPOT_PETITIONS);
    }
}
