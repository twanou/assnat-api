package quebec.salonbleu.assnat.loaders.subjects;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogEntriesSubjectLoader {

    private final ErrorHandler errorHandler;
    private final AssNatLogEntryScraper assNatLogEntryScraper;
    private final SubjectLoader subjectLoader;
    private final SubjectRepository subjectRepository;
    private final UpcomingLogRepository upcomingLogRepository;
    private final AssnatCacheManager assnatCacheManager;

    public CompletableFuture<Void> load(Runnable prerequisiteTask) {
        LocalDate latestInterventionDate = this.subjectRepository.findFirstByOrderByDateDesc().map(Subject::getDate).orElse(LocalDate.MIN);
        List<ScrapedLogEntry> logEntries = this.assNatLogEntryScraper.scrape();
        List<ScrapedLogEntry> filteredLogEntries = logEntries.stream()
                .filter(entry -> entry.getDate().isAfter(latestInterventionDate))
                .filter(entry -> LogType.ASSEMBLY.equals(entry.getType()))
                .filter(entry -> StringUtils.isBlank(entry.getNote()))
                .filter(entry -> LogVersion.FINAL.equals(entry.getVersion()) || LogVersion.PRELIMINARY.equals(entry.getVersion()))
                .sorted(Comparator.comparing(ScrapedLogEntry::getDate))
                .toList();

        this.assertNoDuplicateDate(filteredLogEntries);
        this.loadUpcomingLogs(filteredLogEntries);
        this.assnatCacheManager.clearUpcomingLogCaches();
        return CompletableFuture.runAsync(() -> this.loadFinalLogEntries(prerequisiteTask, filteredLogEntries));
    }

    private void loadUpcomingLogs(List<ScrapedLogEntry> filteredLogEntries) {
        this.upcomingLogRepository.deleteAll();
        filteredLogEntries.forEach(entry -> this.upcomingLogRepository.save(
                UpcomingLog.builder()
                        .date(entry.getDate())
                        .loadingStatus(LogVersion.FINAL.equals(entry.getVersion()))
                        .build()));
    }

    private void loadFinalLogEntries(Runnable prerequisiteTask, List<ScrapedLogEntry> filteredLogEntries) {
        List<ScrapedLogEntry> finalLogEntries = filteredLogEntries.stream()
                .filter(entry -> LogVersion.FINAL.equals(entry.getVersion()))
                .toList();

        if (!finalLogEntries.isEmpty()) {
            prerequisiteTask.run();
            log.info("Début du chargement des journaux");
            finalLogEntries.forEach(entry ->
                    this.subjectLoader.load(entry.getRelativeUrl(), entry.getDate(), entry.getLegislature(), entry.getSession()));
            log.info("Fin du chargement des journaux");
            this.upcomingLogRepository.deleteAllByLoadingStatus(true);
            this.assnatCacheManager.clearLastUpdateCache();
            this.assnatCacheManager.clearUpcomingLogCaches();
        }
    }

    private void assertNoDuplicateDate(List<ScrapedLogEntry> logEntries) {
        Set<LocalDate> dates = logEntries.stream()
                .map(ScrapedLogEntry::getDate)
                .collect(Collectors.toSet());
        this.errorHandler.assertSize(logEntries.size(), dates, () -> new LoadingException("Plusieurs entrées avec la même date."));
    }
}
