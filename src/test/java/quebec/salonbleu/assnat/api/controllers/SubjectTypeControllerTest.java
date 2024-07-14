package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.types.SujetTypeReponse;
import quebec.salonbleu.assnat.api.models.types.TypeDescription;
import quebec.salonbleu.assnat.api.services.SubjectTypeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectTypeControllerTest {

    @Mock
    private SubjectTypeService subjectTypeServiceMock;
    @InjectMocks
    private SubjectTypeController subjectTypeController;

    @Test
    void getSubjectTypes() {
        List<TypeDescription> typeDescriptions = List.of(TypeDescription.of(SujetType.DECLARATION_DEPUTE, "Description"));
        when(subjectTypeServiceMock.getSubjectTypes()).thenReturn(typeDescriptions);

        SujetTypeReponse response = this.subjectTypeController.getSubjectTypes();
        assertSame(typeDescriptions, response.getTypes());
    }
}