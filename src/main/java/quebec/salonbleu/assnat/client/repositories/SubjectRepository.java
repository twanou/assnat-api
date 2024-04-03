package quebec.salonbleu.assnat.client.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;

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

    public List<Subject> findAllById(Iterable<UUID> ids) {
        return this.subjectSpringRepository.findAllById(ids);
    }

    public List<Subject> find(SubjectArgs args, PageRequest pageRequest) {
        Query query = this.getBaseQuery(args);
        Criteria elemMatchCriteria = new Criteria();
        if (!args.getDeputyIds().isEmpty()) {
            elemMatchCriteria.and("deputyId").in(args.getDeputyIds());
        }
        if (!args.getDistrictIds().isEmpty()) {
            elemMatchCriteria.and("districtId").in(args.getDistrictIds());
        }
        if (!args.getPartyIds().isEmpty()) {
            elemMatchCriteria.and("partyId").in(args.getPartyIds());
        }
        query.addCriteria(Criteria.where("subjectDetails.interventions").elemMatch(elemMatchCriteria));
        query.with(pageRequest);
        return mongoTemplate.find(query, Subject.class);
    }

    private Query getBaseQuery(SubjectArgs args) {
        return args.getSearchString()
                .map(searchString -> (Query) TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(searchString)).sortByScore())
                .orElse((new Query().with(Sort.by(Sort.Direction.DESC, "date"))));
    }
}
