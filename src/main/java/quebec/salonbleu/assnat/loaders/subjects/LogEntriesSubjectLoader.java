package quebec.salonbleu.assnat.loaders.subjects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.cache.AssnatCacheManager;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;
import quebec.salonbleu.assnat.client.repositories.SubjectSpringRepository;
import quebec.salonbleu.assnat.client.repositories.UpcomingLogRepository;
import quebec.salonbleu.assnat.scrapers.AssNatLogEntryScraper;
import quebec.salonbleu.assnat.scrapers.models.LogType;
import quebec.salonbleu.assnat.scrapers.models.LogVersion;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogEntry;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogEntriesSubjectLoader {

    private final AssNatLogEntryScraper assNatLogEntryScraper;
    private final SubjectLoader subjectLoader;
    private final SubjectSpringRepository subjectSpringRepository;
    private final UpcomingLogRepository upcomingLogRepository;
    private final AssnatCacheManager assnatCacheManager;

    public void load(Runnable prerequisiteTask) {
        LocalDate latestInterventionDate = this.subjectSpringRepository.findFirstByOrderByDateDesc().map(Subject::getDate).orElse(LocalDate.MIN);
        List<ScrapedLogEntry> logEntries = this.assNatLogEntryScraper.scrape();

        List<ScrapedLogEntry> filteredLogEntries = logEntries.stream()
                .filter(entry -> entry.getDate().isAfter(latestInterventionDate))
                .filter(entry -> LogType.ASSEMBLY.equals(entry.getType()))
                .sorted(Comparator.comparing(ScrapedLogEntry::getDate))
                .toList();

        List<ScrapedLogEntry> finalLogEntries = filteredLogEntries.stream()
                .filter(entry -> LogVersion.FINAL.equals(entry.getVersion()))
                .toList();

        if (!finalLogEntries.isEmpty()) {
            prerequisiteTask.run();
            log.info("Début du chargement des journaux");
            finalLogEntries.forEach(entry ->
                    this.subjectLoader.load(entry.getRelativeUrl(), entry.getDate(), entry.getLegislature(), entry.getSession()));
            this.loadPreliminaryLogEntries(filteredLogEntries);
            this.assnatCacheManager.clearSubjectCaches();
            log.info("Fin du chargement des journaux");
        }
    }

    private void loadPreliminaryLogEntries(List<ScrapedLogEntry> filteredLogEntries) {
        this.upcomingLogRepository.deleteAll();
        List<ScrapedLogEntry> preliminaryLogEntries = filteredLogEntries.stream()
                .filter(entry -> LogVersion.PRELIMINARY.equals(entry.getVersion()))
                .toList();

        preliminaryLogEntries.forEach(entry ->
                this.upcomingLogRepository.save(UpcomingLog.builder().date(entry.getDate()).build()));
    }
}
