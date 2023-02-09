package net.daneau.assnat.loaders.subjects.mappers;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.documents.subdocuments.InterventionDocument;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;
import net.daneau.assnat.loaders.DeputyFinder;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DeputyDeclarationDocumentMapper implements SubjectDocumentTypeMapper {
    private final DeputyFinder deputyFinder;

    private static final String VICE_PRESIDENT = "vice-président";

    @Override
    public List<SubjectDetails> map(ScrapedLogNode logNode) {
        List<SubjectDetails> subjects = new ArrayList<>();
        for (ScrapedLogNode declaration : logNode.getChildren()) {
            Assignment assignment = deputyFinder.findByCompleteName(declaration.getChildren().get(0).getTitle()); // nom complet, ex M. Bob Tremblay
            subjects.add(
                    SubjectDetails.builder()
                            .type(SubjectType.DEPUTY_DECLARATION)
                            .title(declaration.getTitle())
                            .interventions(List.of(
                                    InterventionDocument.builder()
                                            .deputyId(assignment.getDeputyId())
                                            .partyId(assignment.getPartyId())
                                            .districtId(assignment.getDistrictId())
                                            .paragraphs(this.cleanParagraphs(declaration.getChildren().get(0).getParagraphs()))
                                            .build()))
                            .build());
        }
        return subjects;
    }

    @Override
    public List<String> supports() {
        return List.of(AFFAIRES_COURANTES, DECLARATIONS_DE_DEPUTES);
    }

    private List<String> cleanParagraphs(List<String> paragraphs) {
        return IntStream.range(0, this.getEndRange(paragraphs)) // On ignore à partir du moment que le VP se met à parler
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
