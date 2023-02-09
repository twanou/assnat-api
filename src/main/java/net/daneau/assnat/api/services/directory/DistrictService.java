package net.daneau.assnat.api.services.directory;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.client.documents.District;
import net.daneau.assnat.client.repositories.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DistrictService {

    private final DistrictRepository districtRepository;

    Map<String, Circonscription> getCirconscriptions() {
        return this.districtRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(District::getId,
                        d -> Circonscription.builder()
                                .id(d.getId())
                                .nom(d.getName())
                                .build()));
    }
}
