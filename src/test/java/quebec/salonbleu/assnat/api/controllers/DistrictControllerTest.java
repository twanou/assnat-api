package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.api.models.districts.CirconscriptionReponse;
import quebec.salonbleu.assnat.api.services.DistrictService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistrictControllerTest {

    @Mock
    private DistrictService districtServiceMock;
    @InjectMocks
    private DistrictController districtController;

    @Test
    void getDeputiesByName() {
        List<Circonscription> circonscriptions = List.of(Circonscription.builder().build());
        when(districtServiceMock.getDistrictsByName("name")).thenReturn(circonscriptions);

        CirconscriptionReponse response = this.districtController.getDistrictsByName("name");
        assertSame(circonscriptions, response.getCirconscriptions());
    }
}