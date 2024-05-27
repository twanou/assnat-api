package quebec.salonbleu.assnat.loaders.assignments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyLoaderTest {

    @Mock
    private DeputyRepository deputyRepositoryMock;
    @Mock
    private ErrorHandler errorHandlerMock;
    @InjectMocks
    private DeputyLoader deputyLoader;

    @Test
    void loadWithoutSave() {
        Deputy jacques = Deputy.builder().title("M.").firstName("Jacques").lastName("Parizeau").lastDistrict("L'assomption").photo("photo1").build();
        List<Deputy> deputies = new ArrayList<>(List.of(jacques));
        when(deputyRepositoryMock.findAll()).thenReturn(deputies);

        List<Deputy> results = this.deputyLoader.load(List.of(ScrapedDeputy.builder().firstName("Jacques").lastName("Parizeau").district("L'assomption").photo("photo2").build()));
        verify(errorHandlerMock).assertSize(eq(1), eq(List.of(jacques)), argThat(s -> s.get() instanceof LoadingException));
        verify(deputyRepositoryMock).save(jacques.withPhoto("photo2"));
        assertEquals(deputies, results);
    }

    @Test
    void loadWithoutSaveException() {
        Deputy jacques = Deputy.builder().title("M.").firstName("Jacques").lastName("Parizeau").lastDistrict("L'assomption").build();
        List<Deputy> deputies = List.of(jacques);
        when(deputyRepositoryMock.findAll()).thenReturn(deputies);
        doThrow(LoadingException.class).when(errorHandlerMock).assertSize(eq(1), eq(List.of()), any());

        assertThrows(LoadingException.class,
                () -> this.deputyLoader.load(List.of(ScrapedDeputy.builder().firstName("Jacques").lastName("Parizeau").district("Gouin").build())));
        verify(deputyRepositoryMock, never()).save(any());
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
