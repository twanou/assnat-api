package net.daneau.assnat.loaders.assignments;

import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.PartyRepository;
import net.daneau.assnat.scrapers.models.ScrapedDeputy;
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
        List<Party> parties = List.of(Party.builder().name("Parti Québecois").acronym("PQ").build());
        when(partyRepositoryMock.findAll()).thenReturn(parties);

        List<Party> results = this.partyLoader.load(List.of(ScrapedDeputy.builder().party("Parti Québecois").partyAcronym("PQ").build()));
        verify(partyRepositoryMock, never()).save(any());
        assertEquals(parties, results);
    }

    @Test
    void loadWithSave() {
        ScrapedDeputy scrapedDeputy = ScrapedDeputy.builder().party("Parti Québecois").partyAcronym("PQ").build();
        Party newParty = Party.builder().name("Parti Québecois").acronym("PQ").build();
        Party partyInDB = Party.builder().name("Parti Libéral").acronym("PLQ").build();
        when(partyRepositoryMock.findAll()).thenReturn(new ArrayList<>(List.of(partyInDB)));
        when(partyRepositoryMock.save(newParty)).thenReturn(newParty);

        List<Party> results = this.partyLoader.load(List.of(scrapedDeputy));
        assertEquals(List.of(partyInDB, newParty), results);
    }
}