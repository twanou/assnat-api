package net.daneau.assnat.loaders.subjects.mappers;

import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import net.daneau.assnat.loaders.DeputyFinder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionsAnswersDocumentMapper extends SubjectDocumentTypeMapper {

    public QuestionsAnswersDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    protected List<String> format(List<String> paragraphs) {
        return paragraphs;
    }

    @Override
    protected SubjectType getSubjectType() {
        return SubjectType.QUESTIONS_ANSWERS;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, QUESTIONS_REPONSES);
    }
}

