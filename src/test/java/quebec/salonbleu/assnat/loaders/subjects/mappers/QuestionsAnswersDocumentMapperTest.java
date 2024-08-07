package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.DocumentTypeMapper.AFFAIRES_COURANTES;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA.QUESTIONS_REPONSES;

@ExtendWith(MockitoExtension.class)
class QuestionsAnswersDocumentMapperTest {

    @InjectMocks
    private QuestionsAnswersDocumentMapper questionsAnswersDocumentMapper;

    @Test
    void format() {
        List<String> paragraphs = List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose");
        List<String> formattedParagraphs = this.questionsAnswersDocumentMapper.format(paragraphs);
        assertEquals(paragraphs, formattedParagraphs);
    }

    @Test
    public void getSubjectType() {
        assertEquals(SubjectType.QUESTIONS_ANSWERS, this.questionsAnswersDocumentMapper.getSubjectType());
    }

    @Test
    public void supports() {
        assertEquals(List.of(AFFAIRES_COURANTES, QUESTIONS_REPONSES), this.questionsAnswersDocumentMapper.supports());
    }
}