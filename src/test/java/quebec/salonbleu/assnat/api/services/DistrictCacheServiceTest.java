package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.repositories.DistrictRepository;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistrictCacheServiceTest {

    @Mock
    private DistrictRepository districtRepositoryMock;
    @InjectMocks
    private DistrictCacheService districtCacheService;

    @Test
    void getDistricts() {
        District district = District.builder().id(TestUUID.ID1).name("super compté").build();
        when(districtRepositoryMock.findAll()).thenReturn(List.of(district));

        Map<UUID, Circonscription> districtMap = this.districtCacheService.getDistricts();
        assertEquals(district.getId(), districtMap.get(district.getId()).getId());
        assertEquals(district.getName(), districtMap.get(district.getId()).getNom());
    }
}