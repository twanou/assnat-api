package net.daneau.assnat.utils;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.District;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.client.repositories.DeputyRepository;
import net.daneau.assnat.client.repositories.DistrictRepository;
import net.daneau.assnat.client.repositories.PartyRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhotoExtractor {

    private final AssignmentRepository assignmentRepository;
    private final DeputyRepository deputyRepository;
    private final DistrictRepository districtRepository;
    private final PartyRepository partyRepository;
    private final PhotoFileNameGenerator photoFileNameGenerator;
    private static final String PATH = "photos/%s.jpg";

    public void extract() throws IOException {
        List<Assignment> assignments = this.assignmentRepository.findAll(Sort.by(Sort.Direction.DESC, "startDate"));
        Map<String, Deputy> deputies = this.deputyRepository.findAll().stream().collect(Collectors.toMap(Deputy::getId, Function.identity()));
        Map<String, District> districts = this.districtRepository.findAll().stream().collect(Collectors.toMap(District::getId, Function.identity()));
        Map<String, Party> parties = this.partyRepository.findAll().stream().collect(Collectors.toMap(Party::getId, Function.identity()));

        HashSet<String> seen = new HashSet<>();
        for (Assignment assignment : assignments) {
            Deputy deputy = deputies.get(assignment.getDeputyId());
            District district = districts.get(assignment.getDistrictId());
            Party party = parties.get(assignment.getPartyId());
            String fileName = this.photoFileNameGenerator.generate(deputy.getFirstName(), deputy.getLastName(), district.getName(), party.getName());

            if (seen.add(fileName)) {
                byte[] decodedBytes = Base64.getDecoder().decode(assignment.getPhoto());
                FileUtils.writeByteArrayToFile(new File(PATH.formatted(fileName)), decodedBytes);
            }
        }
    }
}
