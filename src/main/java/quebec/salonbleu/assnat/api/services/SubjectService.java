package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.mappers.subjects.SubjectMapper;
import quebec.salonbleu.assnat.api.models.subjects.Sujet;
import quebec.salonbleu.assnat.client.documents.Subject;
import quebec.salonbleu.assnat.client.repositories.SubjectRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final AssignmentService assignmentService;
    private final SubjectMapper subjectMapper;

    public List<Sujet> getSubjectsByDeputyIds(Set<UUID> deputyIds, int pageNumber, int pageSize) {
        List<Subject> subjects = this.subjectRepository.findSubjectsByDeputyIds(deputyIds, PageRequest.of(pageNumber, pageSize));
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    public List<Sujet> getSubjects(Set<UUID> ids) {
        List<Subject> subjects = this.subjectRepository.findAllById(ids);
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }
}
