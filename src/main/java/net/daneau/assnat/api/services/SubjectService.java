package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.mappers.subjects.SubjectMapper;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.subjects.Sujet;
import net.daneau.assnat.api.services.directory.DirectoryService;
import net.daneau.assnat.client.documents.Subject;
import net.daneau.assnat.client.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final DirectoryService directoryService;
    private final SubjectMapper subjectMapper;

    public List<Sujet> getSubjects(Set<String> deputyIds) {
        List<Subject> subjects = this.subjectRepository.findSubjectsByDeputyIds(deputyIds);
        DirectoryDTO roster = this.directoryService.getDirectory();
        return this.subjectMapper.toSujetsList(subjects, roster);
    }
}
