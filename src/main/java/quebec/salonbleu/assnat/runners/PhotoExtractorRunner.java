package quebec.salonbleu.assnat.runners;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.utils.PhotoExtractor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("extractor")
@RequiredArgsConstructor
public class PhotoExtractorRunner implements CommandLineRunner {

    private final PhotoExtractor photoExtractor;

    @Override
    public void run(String... args) throws IOException {
        this.photoExtractor.extract();
    }
}
