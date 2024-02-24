package net.daneau.assnat.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Clock;

@EnableAsync
@EnableCaching
@Configuration
public class AssnatConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
