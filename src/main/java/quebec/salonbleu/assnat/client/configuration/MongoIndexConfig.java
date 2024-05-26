package quebec.salonbleu.assnat.client.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import quebec.salonbleu.assnat.client.documents.Subject;

@Configuration
@RequiredArgsConstructor
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps(Subject.class)
                .ensureIndex(
                        new Index()
                                .on("subjectDetails.interventions.deputyId", Sort.Direction.ASC)
                                .on("date", Sort.Direction.DESC)
                                .named("interventions_deputyId_date")
                );
        mongoTemplate.indexOps(Subject.class)
                .ensureIndex(
                        new Index()
                                .on("subjectDetails.interventions.districtId", Sort.Direction.ASC)
                                .on("date", Sort.Direction.DESC)
                                .named("interventions_districtId_date")
                );
        mongoTemplate.indexOps(Subject.class)
                .ensureIndex(
                        new Index()
                                .on("subjectDetails.interventions.partyId", Sort.Direction.ASC)
                                .on("date", Sort.Direction.DESC)
                                .named("interventions_partyId_date")
                );
        mongoTemplate.indexOps(Subject.class)
                .ensureIndex(
                        TextIndexDefinition.builder()
                                .onFields("subjectDetails.interventions.paragraphs", "subjectDetails.title")
                                .withDefaultLanguage("fr")
                                .build()
                );
    }
}
