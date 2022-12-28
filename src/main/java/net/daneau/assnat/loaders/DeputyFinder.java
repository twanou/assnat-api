package net.daneau.assnat.loaders;


import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.repositories.DeputyRepository;
import net.daneau.assnat.client.repositories.RosterRepository;
import net.daneau.assnat.loaders.events.RosterUpdateEvent;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.utils.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DeputyFinder implements ApplicationListener<RosterUpdateEvent> {

    private final RosterRepository rosterRepository;
    private final DeputyRepository deputyRepository;
    private final ErrorHandler errorHandler;
    private Optional<Cache> cache = Optional.empty();

    private static final String COMPLETE_NAME_FORMAT = "%s %s %s";

    public DeputyFinder(RosterRepository rosterRepository, DeputyRepository deputyRepository, ErrorHandler errorHandler) {
        this.rosterRepository = rosterRepository;
        this.deputyRepository = deputyRepository;
        this.errorHandler = errorHandler;
        this.refreshCache();
    }

    @Override
    public void onApplicationEvent(RosterUpdateEvent event) {
        this.refreshCache();
    }

    public Assignment findByCompleteName(String completeName) {
        List<Deputy> results = this.cache
                .orElseThrow(() -> new LoadingException("La cache de députés n'a pas été initialisée."))
                .deputies.stream()
                .filter(deputy -> StringUtils.equals(formatCompleteName(deputy), completeName))
                .toList();
        this.errorHandler.assertSize(1, results, () -> new LoadingException("Zéro ou plusieurs députés trouvés : " + results));
        Assignment assignment = this.cache.get().assignments.get(results.get(0).getId());
        this.errorHandler.assertNotNull(assignment, () -> new LoadingException("Aucune assignation pour ce député : " + results.get(0)));
        return assignment;
    }

    private void refreshCache() {
        this.cache = this.buildCache();
    }

    private Optional<Cache> buildCache() {
        Optional<Roster> currentRoster = this.rosterRepository.findByEndDate(null);
        if (currentRoster.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Assignment> assignments = currentRoster.get().getAssignments().stream().collect(Collectors.toMap(Assignment::getDeputyId, Function.identity()));
        List<Deputy> deputies = this.deputyRepository.findAllById(assignments.values().stream().map(Assignment::getDeputyId).toList());
        return Optional.of(new Cache(assignments, deputies));
    }

    private String formatCompleteName(Deputy deputy) {
        return String.format(COMPLETE_NAME_FORMAT, deputy.getTitle(), deputy.getFirstName(), deputy.getLastName());
    }

    private record Cache(Map<String, Assignment> assignments, List<Deputy> deputies) {
    }
}
