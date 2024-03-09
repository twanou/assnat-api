package quebec.salonbleu.assnat.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.client.repositories.DistrictRepository;
import quebec.salonbleu.assnat.client.repositories.PartyRepository;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoExtractor {

    private final AssignmentRepository assignmentRepository;
    private final DeputyRepository deputyRepository;
    private final DistrictRepository districtRepository;
    private final PartyRepository partyRepository;
    private final PhotoUtils photoUtils;
    private static final String PATH = "photos/%s.jpg";

    public void extract() throws IOException {
        List<Assignment> assignments = this.assignmentRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
        Map<UUID, Deputy> deputies = this.deputyRepository.findAll().stream().collect(Collectors.toMap(Deputy::getId, Function.identity()));
        Map<UUID, District> districts = this.districtRepository.findAll().stream().collect(Collectors.toMap(District::getId, Function.identity()));
        Map<UUID, Party> parties = this.partyRepository.findAll().stream().collect(Collectors.toMap(Party::getId, Function.identity()));

        HashSet<String> seen = new HashSet<>();
        for (Assignment assignment : assignments) {
            Deputy deputy = deputies.get(assignment.getDeputyId());
            District district = districts.get(assignment.getDistrictId());
            Party party = parties.get(assignment.getPartyId());
            String fileName = this.photoUtils.getUniqueId(deputy.getFirstName(), deputy.getLastName(), district.getName(), party.getName());

            if (seen.add(fileName)) {
                byte[] decodedBytes = Base64.getDecoder().decode(assignment.getPhoto());
                FileUtils.writeByteArrayToFile(new File(PATH.formatted(fileName)), decodedBytes);
            }
        }
    }
}
