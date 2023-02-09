package net.daneau.assnat.api.services.directory;

import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.client.documents.District;
import net.daneau.assnat.client.repositories.DistrictRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepositoryMock;
    @InjectMocks
    private DistrictService districtServiceMock;

    @Test
    void getCirconscriptions() {
        District district = District.builder().id("id").name("super compté").build();
        when(districtRepositoryMock.findAll()).thenReturn(List.of(district));

        Map<String, Circonscription> districtMap = this.districtServiceMock.getCirconscriptions();
        assertEquals(district.getId(), districtMap.get(district.getId()).getId());
        assertEquals(district.getName(), districtMap.get(district.getId()).getNom());
    }
}