package net.daneau.assnat.runners;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.loaders.assignments.AssignmentLoader;
import net.daneau.assnat.loaders.subjects.LogEntriesSubjectLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class LoadRunner implements CommandLineRunner {

    private final AssignmentLoader assignmentLoader;
    private final LogEntriesSubjectLoader subjectLoader;

    @Override
    public void run(String... args) {
        this.assignmentLoader.load();
        this.subjectLoader.load();
    }
}
