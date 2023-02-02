package net.daneau.assnat.client.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@RequiredArgsConstructor
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps(Subject.class)
                .ensureIndex(
                        new Index()
                                .on("subjectData.interventions.deputyId", Sort.Direction.ASC)
                                .on("date", Sort.Direction.DESC)
                                .named("interventions_deputyId_date")
                );
    }
}
