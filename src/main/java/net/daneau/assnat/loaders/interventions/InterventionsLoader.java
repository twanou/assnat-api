package net.daneau.assnat.loaders.interventions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.daneau.assnat.scrappers.AssNatLogScraper;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterventionsLoader {

    private final List<InterventionLoader> interventionLoaders;
    private final AssNatLogScraper assNatLogScraper;

    public void load(ScrapedLogEntry logEntry) {
        log.info("Chargement : " + logEntry);
        ScrapedLogNode logs = this.assNatLogScraper.scrape(logEntry.getRelativeUrl());
        this.interventionLoaders.forEach(loader ->
                this.getLogNodeForMatchers(logs, loader.getInterventionMatchers())
                        .ifPresent(node -> loader.load(logEntry, node))
        );
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
