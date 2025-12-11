package quebec.salonbleu.assnat.loaders;


import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
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

@Slf4j
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
    public Optional<Assignment> findByCompleteName(String completeName) {
        return this.findByFilter(deputy -> Strings.CI.contains(formatCompleteName(deputy), completeName), completeName);
    }

    //M. St-Pierre Plamondon
    public Optional<Assignment> findByLastName(String lastName) {
        return this.findByFilter(deputy -> Strings.CI.contains(formatLastName(deputy), lastName), lastName);
    }

    //M. Plamondon (Camille-Laurin)
    public Optional<Assignment> findByLastNameAndDistrict(String lastName, String district) {
        return this.findByFilter(deputy -> Strings.CI.contains(formatLastName(deputy), lastName) && Strings.CI.contains(deputy.getLastDistrict(), district), lastName + " " + district);
    }

    private Optional<Assignment> findByFilter(Predicate<Deputy> filter, String logInfo) {
        if (this.cache.isEmpty()) {
            this.refreshCache();
        }
        List<Deputy> results = this.cache
                .orElseThrow(() -> new LoadingException("La cache de députés n'a pas été initialisée."))
                .deputies.stream()
                .filter(filter)
                .toList();

        this.errorHandler.assertLessThanEquals(1, results, () -> {
            log.error("Plusieurs députés trouvés : {}, Résultats : {}", logInfo, results);
            throw new LoadingException("Plusieurs députés trouvés : " + logInfo + ", Résultats : " + results);
        });

        if (results.isEmpty()) {
            return Optional.empty();
        }

        Assignment assignment = this.cache.get().assignments.get(results.getFirst().getId());
        this.errorHandler.assertNotNull(assignment, () -> {
            log.error("Aucune assignation pour ce député : {}", results.getFirst());
            return new LoadingException("Aucune assignation pour ce député : " + results.getFirst());
        });
        return Optional.of(assignment);
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
