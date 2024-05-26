package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.api.models.deputies.DeputeReponse;
import quebec.salonbleu.assnat.api.services.DeputyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyControllerTest {

    @Mock
    private DeputyService deputyServiceMock;
    @InjectMocks
    private DeputyController deputyController;

    @Test
    void getDeputiesByName() {
        List<Depute> deputes = List.of(Depute.builder().build());
        when(deputyServiceMock.getDeputiesByName("name")).thenReturn(deputes);

        DeputeReponse response = this.deputyController.getDeputiesByName("name");
        assertSame(deputes, response.getDeputes());
    }
}