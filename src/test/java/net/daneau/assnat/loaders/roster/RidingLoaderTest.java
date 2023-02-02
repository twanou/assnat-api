package net.daneau.assnat.loaders.roster;

import net.daneau.assnat.client.documents.Riding;
import net.daneau.assnat.client.repositories.RidingRepository;
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
class RidingLoaderTest {

    @Mock
    private RidingRepository ridingRepositoryMock;
    @InjectMocks
    private RidingLoader ridingLoader;

    @Test
    void loadWithoutSave() {
        List<Riding> parties = List.of(Riding.builder().name("Camille-Laurin").build());
        when(ridingRepositoryMock.findAll()).thenReturn(parties);

        List<Riding> results = this.ridingLoader.load(List.of(ScrapedDeputy.builder().riding("Camille-Laurin").build()));
        verify(ridingRepositoryMock, never()).save(any());
        assertEquals(parties, results);
    }

    @Test
    void loadWithSave() {
        ScrapedDeputy scrapedDeputy = ScrapedDeputy.builder().riding("Camille-Laurin").build();
        Riding newRiding = Riding.builder().name("Camille-Laurin").build();
        Riding ridingInDB = Riding.builder().name("Rosemont").build();
        when(ridingRepositoryMock.findAll()).thenReturn(new ArrayList<>(List.of(ridingInDB)));
        when(ridingRepositoryMock.save(newRiding)).thenReturn(newRiding);

        List<Riding> results = this.ridingLoader.load(List.of(scrapedDeputy));
        assertEquals(List.of(ridingInDB, newRiding), results);
    }
}