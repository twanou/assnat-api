package net.daneau.assnat.loaders.interventions;

import net.daneau.assnat.scrappers.models.ScrapedLogEntry;
import net.daneau.assnat.scrappers.models.ScrapedLogNode;

import java.util.List;

interface InterventionLoader {

    String AFFAIRES_COURANTES = "Affaires courantes";
    String DECLARATIONS_DE_DEPUTES = "Déclarations de députés";

    void load(ScrapedLogEntry scrapedLogEntry, ScrapedLogNode logNode);

    List<String> getInterventionMatchers();
}
