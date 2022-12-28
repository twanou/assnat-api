package net.daneau.assnat.loaders.roster;

import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.documents.Riding;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.repositories.RosterRepository;
import net.daneau.assnat.loaders.events.RosterUpdateEvent;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.scrappers.DeputyScraper;
import net.daneau.assnat.scrappers.models.ScrapedDeputy;
import net.daneau.assnat.utils.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RosterLoaderTest {

    @Mock
    private ErrorHandler errorHandlerMock;
    @Mock
    private DeputyScraper deputyScraperMock;
    @Mock
    private DeputyLoader deputyLoaderMock;
    @Mock
    private PartyLoader partyLoaderMock;
    @Mock
    private RidingLoader ridingLoaderMock;
    @Mock
    private RosterRepository rosterRepositoryMock;
    @Mock
    private ApplicationEventPublisher eventBusMock;
    @InjectMocks
    private RosterLoader rosterLoader;

    @Test
    void load() {
        List<ScrapedDeputy> scrapedDeputies = List.of(ScrapedDeputy.builder().firstName("Bernard").lastName("Landry").party("Parti Québecois").riding("Verchères").build());
        Roster currentRoster = Roster.builder().hash(42).build();
        Deputy landryDeputy = Deputy.builder().id("deputyId").firstName("Bernard").lastName("Landry").build();
        Riding landryRiding = Riding.builder().id("ridingId").name("Verchères").build();
        Party landryParty = Party.builder().id("partyId").name("Parti Québecois").build();
        when(deputyScraperMock.scrape()).thenReturn(scrapedDeputies);
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(Optional.of(currentRoster));
        when(deputyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDeputy));
        when(ridingLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryRiding));
        when(partyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryParty));

        this.rosterLoader.load();
        InOrder order = Mockito.inOrder(rosterRepositoryMock, eventBusMock);
        order.verify(rosterRepositoryMock).save(Roster.builder()
                .hash(scrapedDeputies.hashCode())
                .startDate(LocalDate.now())
                .assignments(List.of(Assignment.builder().deputyId(landryDeputy.getId()).partyId(landryParty.getId()).ridingId(landryRiding.getId()).build()))
                .build());
        order.verify(eventBusMock).publishEvent(any(RosterUpdateEvent.class));
        verify(rosterRepositoryMock).save(currentRoster.withEndDate(LocalDate.now()));
        verify(errorHandlerMock).assertLessThan(eq(2), eq(List.of(landryDeputy)), argThat(s -> s.get() instanceof LoadingException));
    }

    @Test
    void loadWithoutCurrentRoster() {
        List<ScrapedDeputy> scrapedDeputies = List.of(ScrapedDeputy.builder().firstName("Bernard").lastName("Landry").party("Parti Québecois").riding("Verchères").build());
        Deputy landryDeputy = Deputy.builder().id("deputyId").firstName("Bernard").lastName("Landry").build();
        Riding landryRiding = Riding.builder().id("ridingId").name("Verchères").build();
        Party landryParty = Party.builder().id("partyId").name("Parti Québecois").build();
        when(deputyScraperMock.scrape()).thenReturn(scrapedDeputies);
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(Optional.empty());
        when(deputyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryDeputy));
        when(ridingLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryRiding));
        when(partyLoaderMock.load(scrapedDeputies)).thenReturn(List.of(landryParty));

        this.rosterLoader.load();
        InOrder order = Mockito.inOrder(rosterRepositoryMock, eventBusMock);
        order.verify(rosterRepositoryMock).save(Roster.builder()
                .hash(scrapedDeputies.hashCode())
                .startDate(LocalDate.now())
                .assignments(List.of(Assignment.builder().deputyId(landryDeputy.getId()).partyId(landryParty.getId()).ridingId(landryRiding.getId()).build()))
                .build());
        order.verify(eventBusMock).publishEvent(any(RosterUpdateEvent.class));
        verify(rosterRepositoryMock, atMostOnce()).save(any(Roster.class));
        verify(errorHandlerMock).assertLessThan(eq(2), eq(List.of(landryDeputy)), argThat(s -> s.get() instanceof LoadingException));
    }
}