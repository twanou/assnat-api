package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.DocumentTypeMapper.AFFAIRES_COURANTES;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateB.DEPOT_PETITIONS;

@ExtendWith(MockitoExtension.class)
class PetitionDocumentMapperTest {

    @InjectMocks
    private PetitionDocumentMapper petitionDocumentMapper;

    @Test
    void format() {
        List<String> paragraphs = List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose");
        List<String> formattedParagraphs = this.petitionDocumentMapper.format(paragraphs);
        assertEquals(paragraphs, formattedParagraphs);
    }

    @Test
    public void getSubjectType() {
        assertEquals(SubjectType.PETITION, this.petitionDocumentMapper.getSubjectType());
    }

    @Test
    public void supports() {
        assertEquals(List.of(AFFAIRES_COURANTES, DEPOT_PETITIONS), this.petitionDocumentMapper.supports());
    }
}