package net.daneau.assnat.loaders.subjects.mappers;

import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;

import java.util.List;

public interface SubjectDocumentTypeMapper {

    String AFFAIRES_COURANTES = "Affaires courantes";
    String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";
    String QUESTIONS_REPONSES = "Questions et réponses orales";

    List<SubjectDetails> map(ScrapedLogNode logNode);

    List<String> supports();
}
