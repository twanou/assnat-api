package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.subjects.SujetType;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeMapperTest {

    @Test
    void map() {
        TypeMapper typeMapper = new TypeMapper();
        assertEquals(SujetType.DECLARATION_DEPUTE, typeMapper.map(SubjectType.DEPUTY_DECLARATION));
    }
}