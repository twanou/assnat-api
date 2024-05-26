package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistrictServiceTest {

    @Mock
    private DistrictCacheService districtCacheServiceMock;
    @InjectMocks
    private DistrictService districtService;

    @Test
    void getDistricts() {
        Circonscription circonscription = Circonscription.builder().id(TestUUID.ID1).nom("super compté").build();
        when(districtCacheServiceMock.getDistricts()).thenReturn(Map.of(TestUUID.ID1, circonscription));

        Map<UUID, Circonscription> circonscriptionMap = this.districtService.getDistricts();
        assertEquals(circonscription, circonscriptionMap.get(circonscription.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"super", "compte", "sUpér co"})
    void getDistrictByName(String value) {
        Circonscription circonscription = Circonscription.builder().id(TestUUID.ID1).nom("super compté").build();
        when(districtCacheServiceMock.getDistricts()).thenReturn(Map.of(TestUUID.ID1, circonscription));

        List<Circonscription> circonscriptions = this.districtService.getDistrictsByName(value);
        assertEquals(circonscription, circonscriptions.getFirst());
    }
}