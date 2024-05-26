package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

    @Mock
    private PartyCacheService partyCacheServiceMock;
    @InjectMocks
    private PartyService partyService;

    @Test
    void getParties() {
        Parti parti = Parti.builder().id(TestUUID.ID1).nom("super parti").sigle("SP").build();
        when(partyCacheServiceMock.getParties()).thenReturn(Map.of(TestUUID.ID1, parti));

        Map<UUID, Parti> partiMap = this.partyService.getParties();
        assertEquals(parti, partiMap.get(parti.getId()));
    }


    @ParameterizedTest
    @ValueSource(strings = {"sUpér", "génial", "Sp", "super parti"})
    void getPartiesByName(String value) {
        Parti parti = Parti.builder().id(TestUUID.ID1).nom("super parti génial").sigle("SP").build();
        when(partyCacheServiceMock.getParties()).thenReturn(Map.of(TestUUID.ID1, parti));

        List<Parti> partis = this.partyService.getPartiesByName(value);
        assertEquals(parti, partis.getFirst());
    }
}