package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.PartyRepository;
import test.utils.TestUUID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyCacheServiceTest {

    @Mock
    private PartyRepository partyRepositoryMock;
    @InjectMocks
    private PartyCacheService partyCacheService;

    @Test
    void getParties() {
        Party party = Party.builder().id(TestUUID.ID1).name("super parti").acronym("SP").build();
        when(partyRepositoryMock.findAll()).thenReturn(List.of(party));

        Map<UUID, Parti> partiMap = this.partyCacheService.getParties();
        assertEquals(party.getId(), partiMap.get(party.getId()).getId());
        assertEquals(party.getName(), partiMap.get(party.getId()).getNom());
        assertEquals(party.getAcronym(), partiMap.get(party.getId()).getSigle());
    }
}