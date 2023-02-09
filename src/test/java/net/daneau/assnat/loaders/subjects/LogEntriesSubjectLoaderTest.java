package net.daneau.assnat.loaders.subjects;

import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import net.daneau.assnat.scrappers.AssNatLogEntryScraper;
import net.daneau.assnat.scrappers.models.LogType;
import net.daneau.assnat.scrappers.models.LogVersion;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogEntriesSubjectLoaderTest {
    @Mock
    private AssNatLogEntryScraper assNatLogEntryScraperMock;
    @Mock
    private SubjectLoader subjectLoaderMock;
    @Mock
    private SubjectRepository subjectRepositoryMock;
    @InjectMocks
    private LogEntriesSubjectLoader logEntriesSubjectLoader;

    @ParameterizedTest
    @NullSource
    @MethodSource("subjects")
    void load(Subject subject) {
        ScrapedLogEntry firstEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(1997, 1, 1)).relativeUrl("relativeUrl1").type(LogType.ASSEMBLY).version(LogVersion.FINAL).build();
        ScrapedLogEntry secondEntryToLoad = ScrapedLogEntry.builder().date(LocalDate.of(2010, 1, 1)).relativeUrl("relativeUrl2").type(LogType.ASSEMBLY).version(LogVersion.FINAL).build();
        when(subjectRepositoryMock.findFirstByOrderByDateDesc()).thenReturn(Optional.ofNullable(subject));
        when(assNatLogEntryScraperMock.scrape()).thenReturn(List.of(
                ScrapedLogEntry.builder().date(LocalDate.of(1980, 5, 20)).build(),
                ScrapedLogEntry.builder().date(LocalDate.of(1996, 5, 14)).type(LogType.COMMITTEE).build(),
                ScrapedLogEntry.builder().date(LocalDate.of(2022, 7, 18)).version(LogVersion.PRELIMINARY).build(),
                secondEntryToLoad,
                firstEntryToLoad
        ));

        this.logEntriesSubjectLoader.load();
        InOrder order = inOrder(subjectLoaderMock);
        order.verify(subjectLoaderMock).load(firstEntryToLoad.getRelativeUrl(), firstEntryToLoad.getDate(), firstEntryToLoad.getLegislature(), firstEntryToLoad.getSession());
        order.verify(subjectLoaderMock).load(secondEntryToLoad.getRelativeUrl(), secondEntryToLoad.getDate(), secondEntryToLoad.getLegislature(), secondEntryToLoad.getSession());
    }

    private static Stream<Subject> subjects() {
        return Stream.of(
                Subject.builder().date(LocalDate.of(1995, 10, 30)).build()
        );
    }
}