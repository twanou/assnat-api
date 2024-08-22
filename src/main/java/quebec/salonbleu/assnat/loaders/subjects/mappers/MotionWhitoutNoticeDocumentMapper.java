package quebec.salonbleu.assnat.loaders.subjects.mappers;

import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.loaders.DeputyFinder;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.TemplateC;

import java.util.List;

@Component
public class MotionWhitoutNoticeDocumentMapper extends TemplateC {

    public MotionWhitoutNoticeDocumentMapper(DeputyFinder deputyFinder) {
        super(deputyFinder);
    }

    @Override
    public List<String> format(List<String> paragraphs) {
        return paragraphs;
    }

    @Override
    public SubjectType getSubjectType() {
        return SubjectType.MOTION_WITHOUT_NOTICE;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, MOTIONS_SANS_PREAVIS);
    }
}
