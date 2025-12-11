package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictCacheService districtCacheService;

    public Map<UUID, Circonscription> getDistricts() {
        return this.districtCacheService.getDistricts();
    }

    public List<Circonscription> getDistrictsByName(String name) {
        return this.districtCacheService.getDistricts().values().stream()
                .filter(district ->
                        Strings.CI.contains(
                                StringUtils.stripAccents(district.getNom()),
                                StringUtils.stripAccents(name)))
                .toList();
    }
}
