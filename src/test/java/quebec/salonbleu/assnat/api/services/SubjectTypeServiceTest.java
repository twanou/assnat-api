package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import quebec.salonbleu.assnat.api.models.subjects.SujetType;
import quebec.salonbleu.assnat.api.models.types.TypeDescription;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectTypeServiceTest {

    @Mock
    private MessageSource messageSourceMock;
    @InjectMocks
    private SubjectTypeService subjectTypeService;

    @Test
    void getSubjectTypes() {
        Arrays.stream(SujetType.values())
                .forEach(type -> when(messageSourceMock.getMessage(type.name(), null, Locale.CANADA_FRENCH)).thenReturn("Description"));

        List<TypeDescription> typeDescriptions = this.subjectTypeService.getSubjectTypes();

        Arrays.stream(SujetType.values())
                .forEach(type -> {

                    Optional<TypeDescription> typeDescription = typeDescriptions.stream().filter(t -> t.getType().equals(type)).findFirst();
                    assertTrue(typeDescription.isPresent());
                    assertEquals("Description", typeDescription.get().getDescription());
                });
    }
}