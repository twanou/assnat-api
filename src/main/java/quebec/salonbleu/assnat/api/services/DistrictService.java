package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.repositories.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public Map<String, Circonscription> getDistricts() {
        return this.districtRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(District::getId,
                        d -> Circonscription.builder()
                                .id(d.getId())
                                .nom(d.getName())
                                .build()));
    }
}