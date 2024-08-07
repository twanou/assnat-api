package quebec.salonbleu.assnat.loaders.subjects;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.cache.AssnatCacheManager;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.client.repositories.UpcomingLogRepository;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.scrapers.AssNatLogEntryScraper;
import quebec.salonbleu.assnat.scrapers.models.LogType;
import quebec.salonbleu.assnat.scrapers.models.LogVersion;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogEntry;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogEntriesSubjectLoaderTest {

    @Mock
    private ErrorHandler errorHandlerMock;
    @Mock
    private AssNatLogEntryScraper assNatLogEntryScraperMock;
    @Mock
    private SubjectLoader subjectLoaderMock;
    @Mock
    private SubjectRepository subjectRepositoryMock;
    @Mock
    private UpcomingLogRepository upcomingLogRepositoryMock;
    @Mock
    private AssnatCacheManager assnatCacheManagerMock;
    @InjectMocks
    private LogEntriesSubjectLoader logEntriesSubjectLoader;
    @Mock
    private Runnable runnableMock;

    @NullSource
    @ParameterizedTest
    @MethodSource("subjects")
    void load(Subject subject) throws ExecutionException, InterruptedException {
        ScrapedLogEntry firstEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(1997, 1, 1)).relativeUrl("relativeUrl1").type(LogType.ASSEMBLY).version(LogVersion.FINAL).build();
        ScrapedLogEntry secondEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(2010, 1, 1)).relativeUrl("relativeUrl2").type(LogType.ASSEMBLY).version(LogVersion.FINAL).build();
        ScrapedLogEntry firstPreliminaryEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(2021, 7, 18)).type(LogType.ASSEMBLY).version(LogVersion.PRELIMINARY).build();
        ScrapedLogEntry secondPreliminaryEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(2022, 7, 18)).type(LogType.ASSEMBLY).version(LogVersion.PRELIMINARY).build();
        List<ScrapedLogEntry> scrapedLogEntries = List.of(
                ScrapedLogEntry.builder().date(LocalDate.of(1980, 5, 20)).build(),
                ScrapedLogEntry.builder().date(LocalDate.of(1996, 5, 14)).type(LogType.COMMITTEE).version(LogVersion.FINAL).build(),
                ScrapedLogEntry.builder().date(LocalDate.of(1996, 5, 14)).type(LogType.COMMITTEE).version(LogVersion.FINAL).build(),
                secondPreliminaryEntryToLoad,
                firstPreliminaryEntryToLoad,
                ScrapedLogEntry.builder().date(LocalDate.of(2021, 7, 18)).type(LogType.ASSEMBLY).version(LogVersion.FINAL).note("édition spéciale").build(),
                secondEntryToLoad,
                firstEntryToLoad
        );
        when(subjectRepositoryMock.findFirstByOrderByDateDesc()).thenReturn(Optional.ofNullable(subject));
        when(assNatLogEntryScraperMock.scrape()).thenReturn(scrapedLogEntries);

        this.logEntriesSubjectLoader.load(runnableMock).get();
        InOrder order = inOrder(errorHandlerMock, runnableMock, subjectLoaderMock, upcomingLogRepositoryMock, assnatCacheManagerMock);
        order.verify(errorHandlerMock).assertSize(
                eq(4),
                eq(Set.of(firstEntryToLoad.getDate(), secondEntryToLoad.getDate(), firstPreliminaryEntryToLoad.getDate(), secondPreliminaryEntryToLoad.getDate())),
                ArgumentMatchers.<Supplier<LoadingException>>any()
        );
        order.verify(upcomingLogRepositoryMock).deleteAll();
        order.verify(upcomingLogRepositoryMock).save(UpcomingLog.builder().loadingStatus(true).date(firstEntryToLoad.getDate()).build());
        order.verify(upcomingLogRepositoryMock).save(UpcomingLog.builder().loadingStatus(true).date(secondEntryToLoad.getDate()).build());
        order.verify(upcomingLogRepositoryMock).save(UpcomingLog.builder().date(firstPreliminaryEntryToLoad.getDate()).build());
        order.verify(upcomingLogRepositoryMock).save(UpcomingLog.builder().date(secondPreliminaryEntryToLoad.getDate()).build());
        order.verify(assnatCacheManagerMock).clearUpcomingLogCaches();
        order.verify(runnableMock).run();
        order.verify(subjectLoaderMock).load(firstEntryToLoad.getRelativeUrl(), firstEntryToLoad.getDate(), firstEntryToLoad.getLegislature(), firstEntryToLoad.getSession());
        order.verify(subjectLoaderMock).load(secondEntryToLoad.getRelativeUrl(), secondEntryToLoad.getDate(), secondEntryToLoad.getLegislature(), secondEntryToLoad.getSession());
        order.verify(assnatCacheManagerMock).clearLastUpdateCache();
        order.verify(assnatCacheManagerMock).clearUpcomingLogCaches();
    }

    private static Stream<Subject> subjects() {
        return Stream.of(
                Subject.builder().date(LocalDate.of(1995, 10, 30)).build()
        );
    }
}
