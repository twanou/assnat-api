package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DeputyDeclarationDocumentMapperTest {

    @InjectMocks
    private DeputyDeclarationDocumentMapper deputyDeclarationDocumentMapper;

    @Test
    void format() {
        List<String> formattedParagraphs = this.deputyDeclarationDocumentMapper.format(List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose"));
        assertEquals(List.of("M. Bouchard : Bonjour", "oui"), formattedParagraphs);
    }
}