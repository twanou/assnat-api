package quebec.salonbleu.assnat.api.mappers.subjects;

import org.junit.jupiter.api.Test;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeMapperTest {

    private final TypeMapper typeMapper = new TypeMapper();

    @Test
    void mapSubjectType() {
        assertEquals(SujetType.DECLARATION_DEPUTE, typeMapper.map(SubjectType.DEPUTY_DECLARATION));
    }

    @Test
    void mapSujetType() {
        assertEquals(SubjectType.DEPUTY_DECLARATION, typeMapper.map(SujetType.DECLARATION_DEPUTE));
    }
}