package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Depute;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeputyService {

    private final DeputyCacheService deputyCacheService;

    public Map<UUID, Depute> getDeputies() {
        return this.deputyCacheService.getDeputies();
    }

    public List<Depute> getDeputiesByName(String name) {
        return this.deputyCacheService.getDeputies().values().stream()
                .filter(deputy ->
                        StringUtils.containsIgnoreCase(
                                StringUtils.stripAccents(deputy.getPrenom() + " " + deputy.getNom()),
                                StringUtils.stripAccents(name)))
                .toList();
    }
}
