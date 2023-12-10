package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.mappers.subjects.SubjectMapper;
import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final AssignmentService assignmentService;
    private final SubjectMapper subjectMapper;

    public List<Sujet> getSubjectsByDeputyIds(Set<String> deputyIds) {
        List<Subject> subjects = this.subjectRepository.findSubjectsByDeputyIds(deputyIds);
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }

    public List<Sujet> getSubjects(Set<String> ids) {
        List<Subject> subjects = this.subjectRepository.findAllById(ids);
        return this.subjectMapper.toSujetsList(subjects, this.assignmentService.getAllAssignments());
    }
}
