package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.mappers.subjects.SubjectMapper;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.cache.CacheKey;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;
import quebec.salonbleu.assnat.client.repositories.SubjectSpringRepository;
import quebec.salonbleu.assnat.client.repositories.UpcomingLogRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectSpringRepository subjectSpringRepository;
    private final UpcomingLogRepository upcomingLogRepository;
    private final AssignmentService assignmentService;
    private final SubjectMapper subjectMapper;

    public List<Sujet> getSubjectsByDeputyIds(Set<UUID> deputyIds, int pageNumber, int pageSize) {
        List<Subject> subjects = this.subjectSpringRepository.findSubjectsByDeputyIds(deputyIds, PageRequest.of(pageNumber, pageSize));
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    public List<Sujet> getSubjects(Set<UUID> ids) {
        List<Subject> subjects = this.subjectSpringRepository.findAllById(ids);
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    @Cacheable(CacheKey.Constants.LAST_UPDATE)
    public LocalDate getLastUpdate() {
        return this.subjectSpringRepository.findFirstByOrderByDateDesc()
                .map(Subject::getDate)
                .orElse(null);
    }

    @Cacheable(CacheKey.Constants.NEXT_UPDATE)
    public List<LocalDate> getNextUpdates() {
        return this.upcomingLogRepository.findAll(Sort.by(Sort.Direction.ASC, "date"))
                .stream()
                .map(UpcomingLog::getDate)
                .toList();
    }
}
