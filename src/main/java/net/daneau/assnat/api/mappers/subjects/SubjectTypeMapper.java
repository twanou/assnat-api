package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;

import java.util.EnumSet;

public interface SubjectTypeMapper {

    SujetDetails map(SubjectDetails subjectDetails, DirectoryDTO directoryDTO);

    EnumSet<SubjectType> supports();
}
