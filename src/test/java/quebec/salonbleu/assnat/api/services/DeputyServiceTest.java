package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyServiceTest {

    @Mock
    private DeputyCacheService deputyCacheServiceMock;
    @InjectMocks
    private DeputyService deputyService;

    @Test
    void getDeputies() {
        Depute depute = Depute.builder().id(TestUUID.ID1).prenom("boby").nom("nault").titre("M.").build();
        when(deputyCacheServiceMock.getDeputies()).thenReturn(Map.of(TestUUID.ID1, depute));

        Map<UUID, Depute> deputeMap = this.deputyService.getDeputies();
        assertEquals(depute, deputeMap.get(depute.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"boby", "bô", "nault", "bObY nault"})
    void getDeputiesByName(String value) {
        Depute depute = Depute.builder().id(TestUUID.ID1).prenom("boby").nom("nault").titre("M.").build();
        when(deputyCacheServiceMock.getDeputies()).thenReturn(Map.of(TestUUID.ID1, depute));

        List<Depute> deputes = this.deputyService.getDeputiesByName(value);
        assertEquals(depute, deputes.getFirst());
    }
}