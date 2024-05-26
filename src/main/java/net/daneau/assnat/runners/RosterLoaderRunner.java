package net.daneau.assnat.runners;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.loaders.LogEntryLoader;
import net.daneau.assnat.loaders.roster.RosterLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class RosterLoaderRunner implements CommandLineRunner {

    private final RosterLoader rosterLoader;
    private final LogEntryLoader logEntryLoader;

    @Override
    public void run(String... args) {
        this.rosterLoader.load();
        this.logEntryLoader.load();
    }
}
