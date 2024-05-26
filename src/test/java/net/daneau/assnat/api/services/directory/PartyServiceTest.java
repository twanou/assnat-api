package net.daneau.assnat.api.services.directory;

import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.PartyRepository;
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
class PartyServiceTest {
    @Mock
    private PartyRepository partyRepositoryMock;
    @InjectMocks
    private PartyService partyServiceMock;

    @Test
    void getParties() {
        Party party = Party.builder().id("id").name("super parti").acronym("SP").build();
        when(partyRepositoryMock.findAll()).thenReturn(List.of(party));

        Map<String, Parti> partiMap = this.partyServiceMock.getParties();
        assertEquals(party.getId(), partiMap.get(party.getId()).getId());
        assertEquals(party.getName(), partiMap.get(party.getId()).getNom());
        assertEquals(party.getAcronym(), partiMap.get(party.getId()).getSigle());
    }
}