package net.daneau.assnat.loaders.roster;

import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.PartyRepository;
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
class PartyLoaderTest {

    @Mock
    private PartyRepository partyRepositoryMock;
    @InjectMocks
    private PartyLoader partyLoader;

    @Test
    void loadWithoutSave() {
        List<Party> parties = List.of(Party.builder().name("Parti Québecois").build());
        when(partyRepositoryMock.findAll()).thenReturn(parties);

        List<Party> results = this.partyLoader.load(List.of(ScrapedDeputy.builder().party("Parti Québecois").build()));
        verify(partyRepositoryMock, never()).save(any());
        assertEquals(parties, results);
    }

    @Test
    void loadWithSave() {
        ScrapedDeputy scrapedDeputy = ScrapedDeputy.builder().party("Parti Québecois").build();
        Party newParty = Party.builder().name("Parti Québecois").build();
        Party partyInDB = Party.builder().name("Parti Libéral").build();
        when(partyRepositoryMock.findAll()).thenReturn(new ArrayList<>(List.of(partyInDB)));
        when(partyRepositoryMock.save(newParty)).thenReturn(newParty);

        List<Party> results = this.partyLoader.load(List.of(scrapedDeputy));
        assertEquals(List.of(partyInDB, newParty), results);
    }
}