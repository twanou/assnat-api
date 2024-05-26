package quebec.salonbleu.assnat.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import quebec.salonbleu.assnat.api.models.parties.PartiReponse;
import quebec.salonbleu.assnat.api.services.PartyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyControllerTest {

    @Mock
    private PartyService partyServiceMock;
    @InjectMocks
    private PartyController partyController;

    @Test
    void getDeputiesByName() {
        List<Parti> partis = List.of(Parti.builder().build());
        when(partyServiceMock.getPartiesByName("name")).thenReturn(partis);

        PartiReponse response = this.partyController.getPartiesByName("name");
        assertSame(partis, response.getPartis());
    }
}