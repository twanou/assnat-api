package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeMapperTest {

    @Test
    void map() {
        TypeMapper typeMapper = new TypeMapper();
        assertEquals(SujetType.DECLARATION_DEPUTE, typeMapper.map(SubjectType.DEPUTY_DECLARATION));
    }
}