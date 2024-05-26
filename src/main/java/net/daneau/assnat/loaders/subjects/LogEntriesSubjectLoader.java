package net.daneau.assnat.loaders.subjects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import net.daneau.assnat.scrapers.AssNatLogEntryScraper;
import net.daneau.assnat.scrapers.models.LogType;
import net.daneau.assnat.scrapers.models.LogVersion;
import net.daneau.assnat.scrapers.models.ScrapedLogEntry;
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

    public void load() {
        LocalDate latestInterventionDate = this.subjectRepository.findFirstByOrderByDateDesc().map(Subject::getDate).orElse(LocalDate.MIN);
        List<ScrapedLogEntry> logEntries = this.assNatLogEntryScraper.scrape();
        log.info("Début du chargement des journaux");
        logEntries.stream()
                .filter(entry -> entry.getDate().isAfter(latestInterventionDate))
                .filter(entry -> LogType.ASSEMBLY.equals(entry.getType()))
                .filter(entry -> LogVersion.FINAL.equals(entry.getVersion()))
                .sorted(Comparator.comparing(ScrapedLogEntry::getDate))
                .forEachOrdered(entry -> this.subjectLoader.load(entry.getRelativeUrl(), entry.getDate(), entry.getLegislature(), entry.getSession()));
        log.info("Fin du chargement des journaux");
    }
}
