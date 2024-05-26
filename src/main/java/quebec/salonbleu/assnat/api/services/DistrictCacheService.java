package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.cache.CacheKey;
import quebec.salonbleu.assnat.client.documents.District;
import quebec.salonbleu.assnat.client.repositories.DistrictRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DistrictCacheService {

    private final DistrictRepository districtRepository;

    @Cacheable(CacheKey.Constants.DISTRICTS)
    public Map<UUID, Circonscription> getDistricts() {
        return this.districtRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(District::getId,
                        d -> Circonscription.builder()
                                .id(d.getId())
                                .nom(d.getName())
                                .build()));
    }
}
