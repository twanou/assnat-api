package net.daneau.assnat.loaders.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.loaders.assignments.AssignmentLoader;
import net.daneau.assnat.loaders.subjects.LogEntriesSubjectLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    public void load() {
        if (this.clock.instant().getEpochSecond() - this.lastCheck >= this.checkInterval.getSeconds()) {
            this.lastCheck = this.clock.instant().getEpochSecond();
            this.subjectLoader.load(this.assignmentLoader::load);
        }
    }
}
