package net.daneau.assnat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AssnatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssnatApplication.class, args);
    }
}
