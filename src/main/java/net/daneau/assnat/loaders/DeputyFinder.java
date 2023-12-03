package net.daneau.assnat.loaders;


import jakarta.annotation.Nonnull;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.client.repositories.DeputyRepository;
import net.daneau.assnat.loaders.events.AssignmentUpdateEvent;
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
public class DeputyFinder implements ApplicationListener<AssignmentUpdateEvent> {

    private final AssignmentRepository assignmentRepository;
    private final DeputyRepository deputyRepository;
    private final ErrorHandler errorHandler;
    private Optional<Cache> cache = Optional.empty();

    private static final String COMPLETE_NAME_FORMAT = "%s %s %s";

    public DeputyFinder(AssignmentRepository assignmentRepository, DeputyRepository deputyRepository, ErrorHandler errorHandler) {
        this.assignmentRepository = assignmentRepository;
        this.deputyRepository = deputyRepository;
        this.errorHandler = errorHandler;
        this.refreshCache();
    }

    @Override
    public void onApplicationEvent(@Nonnull AssignmentUpdateEvent event) {
        this.refreshCache();
    }

    public Assignment findByCompleteName(String completeName) {
        List<Deputy> results = this.cache
                .orElseThrow(() -> new LoadingException("La cache de députés n'a pas été initialisée."))
                .deputies.stream()
                .filter(deputy -> StringUtils.equals(formatCompleteName(deputy), completeName))
                .toList();
        this.errorHandler.assertSize(1, results, () -> new LoadingException("Zéro ou plusieurs députés trouvé  : " + completeName + " : " + results));
        Assignment assignment = this.cache.get().assignments.get(results.get(0).getId());
        this.errorHandler.assertNotNull(assignment, () -> new LoadingException("Aucune assignation pour ce député : " + results.get(0)));
        return assignment;
    }

    private void refreshCache() {
        this.cache = this.buildCache();
    }

    private Optional<Cache> buildCache() {
        List<Assignment> currentAssignments = this.assignmentRepository.findByEndDate(null);
        if (currentAssignments.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Assignment> assignments = currentAssignments.stream().collect(Collectors.toMap(Assignment::getDeputyId, Function.identity()));
        List<Deputy> deputies = this.deputyRepository.findAllById(assignments.values().stream().map(Assignment::getDeputyId).toList());
        return Optional.of(new Cache(assignments, deputies));
    }

    private String formatCompleteName(Deputy deputy) {
        return String.format(COMPLETE_NAME_FORMAT, deputy.getTitle(), deputy.getFirstName(), deputy.getLastName());
    }

    private record Cache(Map<String, Assignment> assignments,
                         List<Deputy> deputies) {
    }
}
