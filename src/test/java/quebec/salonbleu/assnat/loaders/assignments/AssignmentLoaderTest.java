package quebec.salonbleu.assnat.loaders.assignments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.cache.AssnatCacheManager;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.scrapers.DeputyScraper;
import quebec.salonbleu.assnat.scrapers.models.ScrapedDeputy;
import test.utils.TestUUID;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentLoaderTest {

    @Mock
    private DeputyScraper deputyScraperMock;
    @Mock
    private DeputyLoader deputyLoaderMock;
    @Mock
    private PartyLoader partyLoaderMock;
    @Mock
    private DistrictLoader districtLoaderMock;
    @Mock
    private AssignmentRepository assignmentRepositoryMock;
    @Mock
    private AssnatCacheManager assnatCacheManagerMock;
    @InjectMocks
    private AssignmentLoader assignmentLoader;

    @Test
    void load() {
        List<ScrapedDeputy> scrapedDeputies = List.of(ScrapedDeputy.builder().firstName("Bernard").lastName("Landry").party("Parti Québecois").district("Verchères").build(), ScrapedDeputy.builder().firstName("René").build());
        List<Assignment> currentAssignments = List.of(Assignment.builder().hash(1733353033).build(), Assignment.builder().hash(1995).build());
        Deputy landryDeputy = Deputy.builder().id(TestUUID.ID1).firstName("Bernard").lastName("Landry").lastDistrict("Verchères").build();
        District landryDistrict = District.builder().id(TestUUID.ID2).name("Verchères").build();
        Party landryParty = Party.builder().id(TestUUID.ID3).name("Parti Québecois").build();
        when(deputyScraperMock.scrape()).thenReturn(scrapedDeputies);
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(currentAssignments);
        when(deputyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDeputy));
        when(districtLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDistrict));
        when(partyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryParty));

        this.assignmentLoader.load();
        InOrder order = Mockito.inOrder(assignmentRepositoryMock, assnatCacheManagerMock);
        order.verify(assignmentRepositoryMock).save(Assignment.builder()
                .hash(scrapedDeputies.get(0).hashCode())
                .startDate(LocalDate.now())
                .deputyId(landryDeputy.getId())
                .partyId(landryParty.getId())
                .districtId(landryDistrict.getId())
                .build());
        order.verify(assignmentRepositoryMock).save(Assignment.builder()
                .hash(1995)
                .endDate(LocalDate.now())
                .build());
        order.verify(assnatCacheManagerMock).clearAllCaches();
    }

    @Test
    void loadWithoutCurrentAssignments() {
        List<ScrapedDeputy> scrapedDeputies = List.of(ScrapedDeputy.builder().firstName("Bernard").lastName("Landry").party("Parti Québecois").district("Verchères").build());
        Deputy landryDeputy = Deputy.builder().id(TestUUID.ID1).firstName("Bernard").lastName("Landry").lastDistrict("Verchères").build();
        District landryDistrict = District.builder().id(TestUUID.ID2).name("Verchères").build();
        Party landryParty = Party.builder().id(TestUUID.ID3).name("Parti Québecois").build();
        when(deputyScraperMock.scrape()).thenReturn(scrapedDeputies);
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of());
        when(deputyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDeputy));
        when(districtLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDistrict));
        when(partyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryParty));

        this.assignmentLoader.load();
        InOrder order = Mockito.inOrder(assignmentRepositoryMock, assnatCacheManagerMock);
        order.verify(assignmentRepositoryMock).save(Assignment.builder()
                .hash(scrapedDeputies.get(0).hashCode())
                .startDate(LocalDate.now())
                .deputyId(landryDeputy.getId())
                .partyId(landryParty.getId())
                .districtId(landryDistrict.getId())
                .build());
        order.verify(assnatCacheManagerMock).clearAllCaches();
        verify(assignmentRepositoryMock, atMostOnce()).save(any(Assignment.class));
    }

    @Test
    void loadNoUpdate() {
        List<ScrapedDeputy> scrapedDeputies = List.of(ScrapedDeputy.builder().firstName("René").build());
        List<Assignment> currentAssignments = List.of(Assignment.builder().hash(1733353033).build());
        when(deputyScraperMock.scrape()).thenReturn(scrapedDeputies);
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(currentAssignments);

        this.assignmentLoader.load();
        verify(assnatCacheManagerMock, never()).clearAllCaches();
        verify(assignmentRepositoryMock, never()).save(any(Assignment.class));
    }
}