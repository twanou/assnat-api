package quebec.salonbleu.assnat.loaders.subjects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.scrapers.AssNatLogEntryScraper;
import quebec.salonbleu.assnat.scrapers.models.LogType;
import quebec.salonbleu.assnat.scrapers.models.LogVersion;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogEntry;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogEntriesSubjectLoader {

    private final AssNatLogEntryScraper assNatLogEntryScraper;
    private final SubjectLoader subjectLoader;
    private final SubjectRepository subjectRepository;

    public void load(Runnable prerequisiteTask) {
        LocalDate latestInterventionDate = this.subjectRepository.findFirstByOrderByDateDesc().map(Subject::getDate).orElse(LocalDate.MIN);
        List<ScrapedLogEntry> logEntries = this.assNatLogEntryScraper.scrape();

        List<ScrapedLogEntry> filteredLogEntries = logEntries.stream()
                .filter(entry -> entry.getDate().isAfter(latestInterventionDate))
                .filter(entry -> LogType.ASSEMBLY.equals(entry.getType()))
                .filter(entry -> LogVersion.FINAL.equals(entry.getVersion()))
                .sorted(Comparator.comparing(ScrapedLogEntry::getDate))
                .toList();

        if (!filteredLogEntries.isEmpty()) {
            prerequisiteTask.run();
            log.info("Début du chargement des journaux");
            filteredLogEntries.forEach(entry -> this.subjectLoader.load(entry.getRelativeUrl(), entry.getDate(), entry.getLegislature(), entry.getSession()));
            log.info("Fin du chargement des journaux");
        }

    }
}
