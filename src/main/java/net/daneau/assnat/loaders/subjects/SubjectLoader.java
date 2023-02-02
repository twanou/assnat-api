package net.daneau.assnat.loaders.subjects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import net.daneau.assnat.loaders.subjects.mappers.SubjectDocumentTypeMapper;
import net.daneau.assnat.scrappers.AssNatLogScraper;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectLoader {

    private final List<SubjectDocumentTypeMapper> subjectDocumentTypeMappers;
    private final SubjectRepository subjectRepository;
    private final AssNatLogScraper assNatLogScraper;

    public void load(String relativeUrl, int legislature, int session) {
        log.info("Chargement : " + relativeUrl);
        ScrapedLogNode logs = this.assNatLogScraper.scrape(relativeUrl);
        List<Subject> subjects = new ArrayList<>();
        for (SubjectDocumentTypeMapper mapper : subjectDocumentTypeMappers) {
            this.getLogNodeForMatchers(logs, mapper.supports())
                    .map(mapper::map)
                    .orElse(List.of())
                    .stream()
                    .map(data -> Subject.builder()
                            .date(LocalDate.now())
                            .subjectData(data)
                            .legislature(legislature)
                            .session(session)
                            .build())
                    .forEach(subjects::add);
        }
        this.subjectRepository.saveAll(subjects);
    }

    private Optional<ScrapedLogNode> getLogNodeForMatchers(ScrapedLogNode rootNode, Iterable<String> matchers) {
        ScrapedLogNode currentNode = rootNode;
        Iterator<String> matcherIterator = matchers.iterator();
        while (currentNode != null && matcherIterator.hasNext()) {
            String matcher = matcherIterator.next();
            currentNode = currentNode.getChildren().stream()
                    .filter(node -> StringUtils.equals(node.getTitle(), matcher))
                    .findFirst()
                    .orElse(null);
        }
        return Optional.ofNullable(currentNode);
    }
}
