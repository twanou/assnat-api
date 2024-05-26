package quebec.salonbleu.assnat.loaders.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.loaders.assignments.AssignmentLoader;
import quebec.salonbleu.assnat.loaders.subjects.LogEntriesSubjectLoader;

import java.time.Clock;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LoadingService {

    private final AssignmentLoader assignmentLoader;
    private final LogEntriesSubjectLoader subjectLoader;
    private final Clock clock;
    @Value("${loader.check-interval.duration}")
    private final Duration checkInterval;
    private long lastCheck = 0;

    @Async
    public synchronized void load() {
        if (this.clock.instant().getEpochSecond() - this.lastCheck >= this.checkInterval.getSeconds()) {
            this.lastCheck = this.clock.instant().getEpochSecond();
            this.subjectLoader.load(this.assignmentLoader::load);
        }
    }
}
