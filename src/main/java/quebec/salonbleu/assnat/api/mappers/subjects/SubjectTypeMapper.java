package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.EnumSet;
import java.util.Map;

public interface SubjectTypeMapper {

    SujetDetails map(SubjectDetails subjectDetails, Map<String, Affectation> affectations);

    EnumSet<SubjectType> supports();
}
