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
    }

    @Test
    void mapSujetType() {
        assertEquals(SubjectType.DEPUTY_DECLARATION, this.typeMapper.map(SujetType.DECLARATION_DEPUTE));
        assertEquals(SubjectType.QUESTIONS_ANSWERS, this.typeMapper.map(SujetType.QUESTIONS_REPONSES));
        assertEquals(SubjectType.PETITION, this.typeMapper.map(SujetType.DEPOT_PETITION));
        assertEquals(SubjectType.MINISTERIAL_DECLARATION, this.typeMapper.map(SujetType.DECLARATION_MINISTERIELLE));
    }
}