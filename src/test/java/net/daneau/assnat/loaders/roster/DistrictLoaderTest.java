package net.daneau.assnat.loaders.roster;

import net.daneau.assnat.client.documents.District;
import net.daneau.assnat.client.repositories.DistrictRepository;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistrictLoaderTest {

    @Mock
    private DistrictRepository districtRepositoryMock;
    @InjectMocks
    private DistrictLoader districtLoader;

    @Test
    void loadWithoutSave() {
        List<District> parties = List.of(District.builder().name("Camille-Laurin").build());
        when(districtRepositoryMock.findAll()).thenReturn(parties);

        List<District> results = this.districtLoader.load(List.of(ScrapedDeputy.builder().district("Camille-Laurin").build()));
        verify(districtRepositoryMock, never()).save(any());
        assertEquals(parties, results);
    }

    @Test
    void loadWithSave() {
        ScrapedDeputy scrapedDeputy = ScrapedDeputy.builder().district("Camille-Laurin").build();
        District newDistrict = District.builder().name("Camille-Laurin").build();
        District districtInDB = District.builder().name("Rosemont").build();
        when(districtRepositoryMock.findAll()).thenReturn(new ArrayList<>(List.of(districtInDB)));
        when(districtRepositoryMock.save(newDistrict)).thenReturn(newDistrict);

        List<District> results = this.districtLoader.load(List.of(scrapedDeputy));
        assertEquals(List.of(districtInDB, newDistrict), results);
    }
}