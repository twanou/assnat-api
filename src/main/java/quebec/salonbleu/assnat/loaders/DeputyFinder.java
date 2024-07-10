package quebec.salonbleu.assnat.loaders;


import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.loaders.events.ClearCacheEvent;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.utils.ErrorHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeputyFinder implements ApplicationListener<ClearCacheEvent> {

    private final AssignmentRepository assignmentRepository;
    private final DeputyRepository deputyRepository;
    private final ErrorHandler errorHandler;
    private Optional<Cache> cache = Optional.empty();

    private static final String COMPLETE_NAME_FORMAT = "%s %s %s";
    private static final String LAST_NAME_FORMAT = "%s %s";

    @Override
    public void onApplicationEvent(@Nonnull ClearCacheEvent event) {
        this.refreshCache();
    }

    //M. Paul St-Pierre Plamondon
    public Assignment findByCompleteName(String completeName) {
        return this.findByFilter(deputy -> StringUtils.equals(formatCompleteName(deputy), completeName));
    }

    //M. St-Pierre Plamondon
    public Assignment findByLastName(String lastName) {
        return this.findByFilter(deputy -> StringUtils.equals(formatLastName(deputy), lastName));
    }

    private Assignment findByFilter(Predicate<Deputy> filter) {
        if (this.cache.isEmpty()) {
            this.refreshCache();
        }
        List<Deputy> results = this.cache
                .orElseThrow(() -> new LoadingException("La cache de députés n'a pas été initialisée."))
                .deputies.stream()
                .filter(filter)
                .toList();
        this.errorHandler.assertSize(1, results, () -> new LoadingException("Zéro ou plusieurs députés trouvé : " + results));
        Assignment assignment = this.cache.get().assignments.get(results.getFirst().getId());
        this.errorHandler.assertNotNull(assignment, () -> new LoadingException("Aucune assignation pour ce député : " + results.getFirst()));
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
        Map<UUID, Assignment> assignments = currentAssignments.stream().collect(Collectors.toMap(Assignment::getDeputyId, Function.identity()));
        List<Deputy> deputies = this.deputyRepository.findAllById(assignments.values().stream().map(Assignment::getDeputyId).toList());
        return Optional.of(new Cache(assignments, deputies));
    }

    private String formatCompleteName(Deputy deputy) {
        return String.format(COMPLETE_NAME_FORMAT, deputy.getTitle(), deputy.getFirstName(), deputy.getLastName());
    }

    private String formatLastName(Deputy deputy) {
        return String.format(LAST_NAME_FORMAT, deputy.getTitle(), deputy.getLastName());
    }

    private record Cache(Map<UUID, Assignment> assignments,
                         List<Deputy> deputies) {
    }
}
