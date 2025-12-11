package quebec.salonbleu.assnat.loaders.subjects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.loaders.subjects.mappers.templates.DocumentTypeMapper;
import quebec.salonbleu.assnat.scrapers.AssNatLogScraper;
import quebec.salonbleu.assnat.scrapers.models.ScrapedLogNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectLoader {

    private final List<DocumentTypeMapper> subjectDocumentTypeMappers;
    private final SubjectRepository subjectRepository;
    private final AssNatLogScraper assNatLogScraper;

    public void load(String relativeUrl, LocalDate date, int legislature, int session) {
        log.info("Chargement : {}", relativeUrl);
        ScrapedLogNode logs = this.assNatLogScraper.scrape(relativeUrl);
        List<Subject> subjects = new ArrayList<>();
        for (DocumentTypeMapper mapper : subjectDocumentTypeMappers) {
            this.getLogNodeForMatchers(logs, mapper.supports())
                    .map(mapper::map)
                    .orElse(List.of())
                    .stream()
                    .map(data -> Subject.builder()
                            .pageId(this.getPageId(relativeUrl))
                            .date(date)
                            .subjectDetails(data)
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
                    .filter(node -> Strings.CS.equals(node.getTitle(), matcher))
                    .findFirst()
                    .orElse(null);
        }
        return Optional.ofNullable(currentNode);
    }

    private String getPageId(String relativeUrl) {
        int beginIndex = relativeUrl.lastIndexOf("/") + 1;
        int endIndex = relativeUrl.lastIndexOf(".html");
        return relativeUrl.substring(beginIndex, endIndex);
    }
}
