package quebec.salonbleu.assnat.client.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubjectRepository {

    private final MongoTemplate mongoTemplate;
    private final SubjectSpringRepository subjectSpringRepository;

    public Optional<Subject> findFirstByOrderByDateDesc() {
        return this.subjectSpringRepository.findFirstByOrderByDateDesc();
    }

    public List<Subject> search(String searchString, List<UUID> deputyIds, List<UUID> partyIds, List<UUID> districtIds) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchString);
        Query query = new Query(); //TextQuery.queryText(criteria).sortByScore();

        if (!deputyIds.isEmpty()) {
            //    query.addCriteria(Criteria.where("subjectDetails.interventions").elemMatch(Criteria.where("deputyId").in(deputyIds)));
        }
        if (!partyIds.isEmpty()) {
            query.addCriteria(Criteria.where("subjectDetails.interventions").elemMatch(Criteria.where("partyId").in(partyIds)));
        }
        if (!districtIds.isEmpty()) {
            query.addCriteria(Criteria.where("subjectDetails.interventions").elemMatch(Criteria.where("districtId").in(districtIds)));
        }
        query.with(Sort.by(Sort.Direction.DESC, "date"));
        return mongoTemplate.find(query, Subject.class);
    }
}
