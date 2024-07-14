package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType.DEPUTY_DECLARATION;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.DocumentTypeMapper.AFFAIRES_COURANTES;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateA.DECLARATIONS_DE_DEPUTES;

@ExtendWith(MockitoExtension.class)
class DeputyDeclarationDocumentMapperTest {

    @InjectMocks
    private DeputyDeclarationDocumentMapper deputyDeclarationDocumentMapper;

    @Test
    void format() {
        List<String> formattedParagraphs = this.deputyDeclarationDocumentMapper.format(List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose"));
        assertEquals(List.of("M. Bouchard : Bonjour", "oui"), formattedParagraphs);
    }

    @Test
    public void getSubjectType() {
        assertEquals(DEPUTY_DECLARATION, this.deputyDeclarationDocumentMapper.getSubjectType());
    }

    @Test
    public void supports() {
        assertEquals(List.of(AFFAIRES_COURANTES, DECLARATIONS_DE_DEPUTES), this.deputyDeclarationDocumentMapper.supports());
    }
}