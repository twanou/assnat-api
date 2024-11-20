package quebec.salonbleu.assnat.api.mappers.subjects;

import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.subjects.SujetDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectDetails;
import quebec.salonbleu.assnat.client.documents.subdocuments.SubjectType;

import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

public interface SubjectTypeMapper {

    SujetDetails completeMap(SubjectDetails subjectDetails, Map<UUID, Affectation> affectations);

    SujetDetails partialMap(SubjectDetails subjectDetails, Map<UUID, Affectation> affectations);

    EnumSet<SubjectType> supports();
}
