package quebec.salonbleu.assnat.client.repositories;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubjectRepository {

    private final MongoTemplate mongoTemplate;
    private final SubjectSpringRepository subjectSpringRepository;

    public Optional<Subject> findFirstByOrderByDateDesc() {
        return this.subjectSpringRepository.findFirstByOrderByDateDesc();
    }

    public List<Subject> findSubjectsByDeputyIds(Set<UUID> ids, Pageable pageable) {
        return this.subjectSpringRepository.findSubjectsByDeputyIds(ids, pageable);
    }

    public List<Subject> findAllById(Iterable<UUID> ids) {
        return this.subjectSpringRepository.findAllById(ids);
    }

    public List<Subject> saveAll(Iterable<Subject> subjects) {
        return this.subjectSpringRepository.saveAll(subjects);
    }

    public List<Subject> find(SubjectArgs args, PageRequest pageRequest) {
        Query query = this.getBaseQuery(args);
        List<Document> criteriaObjects = new ArrayList<>();

        args.getDeputyIds().stream()
                .map(deputyId -> new Criteria().elemMatch(Criteria.where("deputyId").is(deputyId)).getCriteriaObject())
                .forEach(criteriaObjects::add);

        args.getDistrictIds().stream()
                .map(deputyId -> new Criteria().elemMatch(Criteria.where("districtId").is(deputyId)).getCriteriaObject())
                .forEach(criteriaObjects::add);

        args.getPartyIds().stream()
                .map(deputyId -> new Criteria().elemMatch(Criteria.where("partyId").is(deputyId)).getCriteriaObject())
                .forEach(criteriaObjects::add);

        if (!criteriaObjects.isEmpty()) {
            query.addCriteria(Criteria.where("subjectDetails.interventions").all(criteriaObjects));
        }

        if (!args.getSubjectTypes().isEmpty()) {
            query.addCriteria(Criteria.where("subjectDetails.type").in(args.getSubjectTypes()));
        }

        query.with(pageRequest);
        return mongoTemplate.find(query, Subject.class);
    }

    private Query getBaseQuery(SubjectArgs args) {
        if (args.getKeywords().isEmpty() && StringUtils.isBlank(args.getPhrase())) {
            return new Query().with(Sort.by(Sort.Direction.DESC, "date"));
        } else {
            return TextQuery.queryText(TextCriteria.forDefaultLanguage()
                            .matchingPhrase(args.getPhrase())
                            .matching(StringUtils.join(args.getKeywords(), " ")))
                    .sortByScore();
        }
    }
}
