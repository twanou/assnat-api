package quebec.salonbleu.assnat.api.services;

import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
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
class DeputyServiceTest {

    @Mock
    private DeputyRepository deputyRepositoryMock;
    @InjectMocks
    private DeputyService deputyServiceMock;

    @Test
    void getDeputes() {
        Deputy deputy = Deputy.builder().id("id").firstName("boby").lastName("nault").title("M.").build();
        when(deputyRepositoryMock.findAll()).thenReturn(List.of(deputy));

        Map<String, Depute> deputeMap = this.deputyServiceMock.getDeputies();
        assertEquals(deputy.getId(), deputeMap.get(deputy.getId()).getId());
        assertEquals(deputy.getTitle(), deputeMap.get(deputy.getId()).getTitre());
        assertEquals(deputy.getFirstName(), deputeMap.get(deputy.getId()).getPrenom());
        assertEquals(deputy.getLastName(), deputeMap.get(deputy.getId()).getNom());
    }
}