package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyCacheServiceTest {

    @Mock
    private DeputyRepository deputyRepositoryMock;
    @InjectMocks
    private DeputyCacheService deputyCacheService;

    @Test
    void getDeputes() {
        Deputy deputy = Deputy.builder().id(TestUUID.ID1).firstName("boby").lastName("nault").title("M.").build();
        when(deputyRepositoryMock.findAll()).thenReturn(List.of(deputy));

        Map<UUID, Depute> deputeMap = this.deputyCacheService.getDeputies();
        assertEquals(deputy.getId(), deputeMap.get(deputy.getId()).getId());
        assertEquals(deputy.getTitle(), deputeMap.get(deputy.getId()).getTitre());
        assertEquals(deputy.getFirstName(), deputeMap.get(deputy.getId()).getPrenom());
        assertEquals(deputy.getLastName(), deputeMap.get(deputy.getId()).getNom());
    }
}