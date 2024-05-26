package net.daneau.assnat.api.services.directory;

import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.commons.Parti;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectoryServiceTest {
    @Mock
    private DeputyService deputyServiceMock;
    @Mock
    private DistrictService districtServiceMock;
    @Mock
    private PartyService partyServiceMock;
    @InjectMocks
    private DirectoryService directoryService;

    @Test
    void getDirectory() {
        Map<String, Depute> deputeMap = new HashMap<>();
        Map<String, Parti> partiMap = new HashMap<>();
        Map<String, Circonscription> circonscriptionMap = new HashMap<>();
        when(deputyServiceMock.getDeputes()).thenReturn(deputeMap);
        when(districtServiceMock.getCirconscriptions()).thenReturn(circonscriptionMap);
        when(partyServiceMock.getParties()).thenReturn(partiMap);

        DirectoryDTO directoryDTO = this.directoryService.getDirectory();
        assertSame(deputeMap, directoryDTO.getDeputies());
        assertSame(partiMap, directoryDTO.getPartis());
        assertSame(circonscriptionMap, directoryDTO.getCirconscriptions());
    }
}