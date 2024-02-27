package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}