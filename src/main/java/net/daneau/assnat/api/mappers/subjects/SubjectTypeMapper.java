package net.daneau.assnat.api.mappers.subjects;

import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.subjects.SujetDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectDetails;
import net.daneau.assnat.client.documents.subdocuments.SubjectType;

import java.util.EnumSet;
import java.util.Map;

public interface SubjectTypeMapper {

    SujetDetails map(SubjectDetails subjectDetails, Map<String, Affectation> affectations);

    EnumSet<SubjectType> supports();
}
