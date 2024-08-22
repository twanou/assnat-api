package quebec.salonbleu.assnat.api.mappers.subjects;

import org.junit.jupiter.api.Test;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeMapperTest {

    private final TypeMapper typeMapper = new TypeMapper();

    @Test
    void mapSubjectType() {
        assertEquals(SujetType.DECLARATION_DEPUTE, this.typeMapper.map(SubjectType.DEPUTY_DECLARATION));
        assertEquals(SujetType.QUESTIONS_REPONSES, this.typeMapper.map(SubjectType.QUESTIONS_ANSWERS));
        assertEquals(SujetType.DEPOT_PETITION, this.typeMapper.map(SubjectType.PETITION));
        assertEquals(SujetType.DECLARATION_MINISTERIELLE, this.typeMapper.map(SubjectType.MINISTERIAL_DECLARATION));
        assertEquals(SujetType.PRESENTATION_PROJET_LOI, this.typeMapper.map(SubjectType.LAW_PROJECT_PRESENTATION));
        assertEquals(SujetType.MOTION_SANS_PREAVIS, this.typeMapper.map(SubjectType.MOTION_WITHOUT_NOTICE));
    }

    @Test
    void mapSujetType() {
        assertEquals(SubjectType.DEPUTY_DECLARATION, this.typeMapper.map(SujetType.DECLARATION_DEPUTE));
        assertEquals(SubjectType.QUESTIONS_ANSWERS, this.typeMapper.map(SujetType.QUESTIONS_REPONSES));
        assertEquals(SubjectType.PETITION, this.typeMapper.map(SujetType.DEPOT_PETITION));
        assertEquals(SubjectType.LAW_PROJECT_PRESENTATION, this.typeMapper.map(SujetType.PRESENTATION_PROJET_LOI));
        assertEquals(SubjectType.MOTION_WITHOUT_NOTICE, this.typeMapper.map(SujetType.MOTION_SANS_PREAVIS));
    }
}