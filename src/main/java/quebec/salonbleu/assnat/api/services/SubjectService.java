package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.mappers.subjects.SubjectMapper;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.api.models.subjects.requests.SujetRequete;
import quebec.salonbleu.assnat.cache.CacheKey;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.documents.UpcomingLog;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;
import quebec.salonbleu.assnat.client.repositories.UpcomingLogRepository;
import quebec.salonbleu.assnat.client.repositories.args.SubjectArgs;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UpcomingLogRepository upcomingLogRepository;
    private final AssignmentService assignmentService;
    private final SubjectMapper subjectMapper;

    public List<Sujet> getSubjectsByDeputyIds(Set<UUID> deputyIds, int pageNumber, int pageSize) {
        List<Subject> subjects = this.subjectRepository.findSubjectsByDeputyIds(deputyIds, PageRequest.of(pageNumber, pageSize));
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    public List<Sujet> getSubjects(SujetRequete sujetRequete) {
        SubjectArgs subjectArgs = this.subjectMapper.toSubjectArgs(sujetRequete);
        List<Subject> subjects = this.subjectRepository.find(subjectArgs, PageRequest.of(sujetRequete.getPage(), sujetRequete.getTaille()));
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    public List<Sujet> getSubjectsById(Set<UUID> ids) {
        List<Subject> subjects = this.subjectRepository.findAllById(ids);
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    @Cacheable(CacheKey.Constants.LAST_UPDATE)
    public LocalDate getLastUpdate() {
        return this.subjectRepository.findFirstByOrderByDateDesc()
                .map(Subject::getDate)
                .orElse(null);
    }

    @Cacheable(CacheKey.Constants.NEXT_UPDATE)
    public List<LocalDate> getNextUpdates() {
        return this.upcomingLogRepository.findAllByLoadingStatus(false, Sort.by(Sort.Direction.ASC, "date"))
                .stream()
                .map(UpcomingLog::getDate)
                .toList();
    }

    @Cacheable(CacheKey.Constants.CURRENTLY_LOADING)
    public List<LocalDate> getCurrentlyLoading() {
        return this.upcomingLogRepository.findAllByLoadingStatus(true, Sort.by(Sort.Direction.ASC, "date"))
                .stream()
                .map(UpcomingLog::getDate)
                .toList();
    }
}
