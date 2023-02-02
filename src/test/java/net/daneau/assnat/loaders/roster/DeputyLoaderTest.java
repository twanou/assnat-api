package net.daneau.assnat.loaders.roster;

import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.repositories.DeputyRepository;
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
class DeputyLoaderTest {

    @Mock
    private DeputyRepository deputyRepositoryMock;
    @InjectMocks
    private DeputyLoader deputyLoader;

    @Test
    void loadWithoutSave() {
        List<Deputy> deputies = List.of(Deputy.builder().title("M.").firstName("Jacques").lastName("Parizeau").build());
        when(deputyRepositoryMock.findAll()).thenReturn(deputies);

        List<Deputy> results = this.deputyLoader.load(List.of(ScrapedDeputy.builder().firstName("Jacques").lastName("Parizeau").build()));
        verify(deputyRepositoryMock, never()).save(any());
        assertEquals(deputies, results);
    }

    @Test
    void loadWithSave() {
        ScrapedDeputy scrapedDeputy = ScrapedDeputy.builder().firstName("Jacques").lastName("Parizeau").build();
        Deputy newDeputy = Deputy.builder().firstName("Jacques").lastName("Parizeau").build();
        Deputy deputyInDB = Deputy.builder().firstName("Jacques").lastName("Croteau").build();
        when(deputyRepositoryMock.findAll()).thenReturn(new ArrayList<>(List.of(deputyInDB)));
        when(deputyRepositoryMock.save(newDeputy)).thenReturn(newDeputy);

        List<Deputy> results = this.deputyLoader.load(List.of(scrapedDeputy));
        assertEquals(List.of(deputyInDB, newDeputy), results);
    }
}