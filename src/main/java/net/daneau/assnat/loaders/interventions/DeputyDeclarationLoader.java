package net.daneau.assnat.loaders.interventions;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Intervention;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.interventions.DeputyDeclaration;
import net.daneau.assnat.client.repositories.InterventionRepository;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
class DeputyDeclarationLoader implements InterventionLoader {

    private final InterventionRepository interventionRepository;
    private final DeputyFinder deputyFinder;

    private static final String VICE_PRESIDENT = "vice-président";

    @Override
    public void load(ScrapedLogEntry logEntry, ScrapedLogNode logNode) {
        for (ScrapedLogNode declaration : logNode.getChildren()) {
            Assignment assignment = deputyFinder.findByCompleteName(declaration.getChildren().get(0).getTitle()); // nom complet, ex M. Bob Tremblay
            this.interventionRepository.save(
                    Intervention.builder()
                            .interventionData(
                                    DeputyDeclaration.builder()
                                            .title(declaration.getTitle())
                                            .paragraphs(this.cleanParagraphs(declaration.getChildren().get(0).getParagraphs()))
                                            .build())
                            .date(logEntry.getDate())
                            .deputyId(assignment.getDeputyId())
                            .partyId(assignment.getPartyId())
                            .ridingId(assignment.getRidingId())
                            .legislature(logEntry.getLegislature())
                            .session(logEntry.getSession())
                            .build());
        }
    }

    @Override
    public List<String> getInterventionMatchers() {
        return List.of(AFFAIRES_COURANTES, DECLARATIONS_DE_DEPUTES);
    }

    private List<String> cleanParagraphs(List<String> paragraphs) {
        return IntStream.range(0, this.getEndRange(paragraphs)) // On ignore à partir du moment que le VP se met a parler
                .mapToObj(i -> i == 0 ? this.removeDeputyName(paragraphs.get(i)) : paragraphs.get(i)) // retrait du nom du député au début du premier paragraphe
                .toList();
    }

    private int getEndRange(List<String> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            if (StringUtils.containsIgnoreCase(paragraphs.get(i), VICE_PRESIDENT)) {
                return i;
            }
        }
        return paragraphs.size();
    }

    private String removeDeputyName(String paragraph) {
        return StringUtils.strip(paragraph.split(":")[1]);
    }
}
