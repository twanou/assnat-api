package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Parti;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyCacheService partyCacheService;

    public Map<UUID, Parti> getParties() {
        return this.partyCacheService.getParties();
    }

    public List<Parti> getPartiesByName(String name) {
        return this.partyCacheService.getParties().values().stream()
                .filter(party ->
                        Strings.CI.contains(
                                StringUtils.stripAccents(party.getNom() + " " + party.getSigle()),
                                StringUtils.stripAccents(name)))
                .toList();
    }
}
