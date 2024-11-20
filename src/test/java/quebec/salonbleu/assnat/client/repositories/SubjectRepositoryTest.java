package quebec.salonbleu.assnat.client.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;
import test.utils.TestUUID;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectRepositoryTest {

    @Mock
    private SubjectSpringRepository subjectSpringRepositoryMock;
    @Mock
    private MongoTemplate mongoTemplateMock;
    @InjectMocks
    private SubjectRepository subjectRepository;

    @Test
    void findFirstByOrderByDateDesc() {
        Subject subject = Subject.builder().build();
        when(subjectSpringRepositoryMock.findFirstByOrderByDateDesc()).thenReturn(Optional.of(subject));

        Optional<Subject> response = this.subjectRepository.findFirstByOrderByDateDesc();
        assertSame(subject, response.orElseThrow());
    }

    @Test
    void findSubjectsByDeputyIds() {
        List<Subject> subjects = List.of(Subject.builder().build());
        when(subjectSpringRepositoryMock.findSubjectsByDeputyIdsOrSubjectTypes(Set.of(TestUUID.ID1, TestUUID.ID2), Set.of("patate", "tofu"), PageRequest.of(0, 25))).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.findSubjectsByDeputyIdsOrSubjectTypes(Set.of(TestUUID.ID1, TestUUID.ID2), Set.of("patate", "tofu"), PageRequest.of(0, 25));
        assertSame(subjects, response);
    }

    @Test
    void findAllById() {
        List<Subject> subjects = List.of(Subject.builder().build());
        when(subjectSpringRepositoryMock.findByIdInOrderByDateDesc(List.of(TestUUID.ID1))).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.findAllById(List.of(TestUUID.ID1));
        assertSame(subjects, response);
    }

    @Test
    void saveAll() {
        List<Subject> subjects = List.of(Subject.builder().build());
        when(subjectSpringRepositoryMock.saveAll(subjects)).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.saveAll(subjects);
        assertSame(subjects, response);
    }

    @Test
    void findWithSearchString() {
        List<Subject> subjects = List.of(Subject.builder().build());
        SubjectArgs args = SubjectArgs.builder()
                .keywords(new LinkedHashSet<>(List.of("mot1", "mot2", "mot3")))
                .deputyIds(new LinkedHashSet<>(List.of(TestUUID.ID1, TestUUID.ID2)))
                .districtIds(new LinkedHashSet<>(List.of(TestUUID.ID3, TestUUID.ID4)))
                .partyIds(new LinkedHashSet<>(List.of(TestUUID.ID5, TestUUID.ID6)))
                .subjectTypes(new LinkedHashSet<>(List.of(SubjectType.DEPUTY_DECLARATION.name(), SubjectType.QUESTIONS_ANSWERS.name())))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 25);
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching("mot1 mot2 mot3")).sortByScore()
                .addCriteria(Criteria.where("subjectDetails.interventions")
                        .all(
                                new Criteria().elemMatch(Criteria.where("deputyId").is(TestUUID.ID1)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("deputyId").is(TestUUID.ID2)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("districtId").is(TestUUID.ID3)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("districtId").is(TestUUID.ID4)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("partyId").is(TestUUID.ID5)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("partyId").is(TestUUID.ID6)).getCriteriaObject()
                        ))
                .addCriteria(new Criteria("subjectDetails.type").in(args.getSubjectTypes()))
                .with(PageRequest.of(0, 25));
        when(mongoTemplateMock.find(query, Subject.class)).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.find(args, pageRequest);
        assertSame(subjects, response);
    }

    @Test
    void findWithoutSearchString() {
        List<Subject> subjects = List.of(Subject.builder().build());
        SubjectArgs args = SubjectArgs.builder()
                .deputyIds(new LinkedHashSet<>(List.of(TestUUID.ID1, TestUUID.ID2)))
                .districtIds(new LinkedHashSet<>(List.of(TestUUID.ID3, TestUUID.ID4)))
                .partyIds(new LinkedHashSet<>(List.of(TestUUID.ID5, TestUUID.ID6)))
                .subjectTypes(new LinkedHashSet<>(List.of(SubjectType.DEPUTY_DECLARATION.name(), SubjectType.QUESTIONS_ANSWERS.name())))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 25);
        Query query = new Query()
                .addCriteria(Criteria.where("subjectDetails.interventions")
                        .all(
                                new Criteria().elemMatch(Criteria.where("deputyId").is(TestUUID.ID1)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("deputyId").is(TestUUID.ID2)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("districtId").is(TestUUID.ID3)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("districtId").is(TestUUID.ID4)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("partyId").is(TestUUID.ID5)).getCriteriaObject(),
                                new Criteria().elemMatch(Criteria.where("partyId").is(TestUUID.ID6)).getCriteriaObject()
                        ))
                .addCriteria(new Criteria("subjectDetails.type").in(args.getSubjectTypes()))
                .with(Sort.by(Sort.Direction.DESC, "date"))
                .with(PageRequest.of(0, 25));
        when(mongoTemplateMock.find(query, Subject.class)).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.find(args, pageRequest);
        assertSame(subjects, response);
    }

    @Test
    void findWithoutElemMatch() {
        List<Subject> subjects = List.of(Subject.builder().build());
        SubjectArgs args = SubjectArgs.builder().build();
        PageRequest pageRequest = PageRequest.of(0, 25);
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "date"))
                .with(PageRequest.of(0, 25));
        when(mongoTemplateMock.find(query, Subject.class)).thenReturn(subjects);

        List<Subject> response = this.subjectRepository.find(args, pageRequest);
        assertSame(subjects, response);
    }
}
