package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.DocumentTypeMapper.AFFAIRES_COURANTES;
import static quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateC.MOTIONS_SANS_PREAVIS;

@ExtendWith(MockitoExtension.class)
class MotionWhitoutNoticeDocumentMapperTest {

    @InjectMocks
    private MotionWhitoutNoticeDocumentMapper motionWhitoutNoticeDocumentMapper;

    @Test
    void format() {
        List<String> paragraphs = List.of("M. Bouchard : Bonjour", "oui", "Vice-Président : merci lulu", "bon passons à autre chose");
        List<String> formattedParagraphs = this.motionWhitoutNoticeDocumentMapper.format(paragraphs);
        assertEquals(paragraphs, formattedParagraphs);
    }

    @Test
    public void getSubjectType() {
        assertEquals(SubjectType.MOTION_WITHOUT_NOTICE, this.motionWhitoutNoticeDocumentMapper.getSubjectType());
    }

    @Test
    public void supports() {
        assertEquals(List.of(AFFAIRES_COURANTES, MOTIONS_SANS_PREAVIS), this.motionWhitoutNoticeDocumentMapper.supports());
    }
}