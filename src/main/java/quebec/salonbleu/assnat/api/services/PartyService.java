package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import quebec.salonbleu.assnat.client.documents.Party;
import quebec.salonbleu.assnat.client.repositories.PartyRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    public Map<UUID, Parti> getParties() {
        return this.partyRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Party::getId,
                        p -> Parti.builder()
                                .id(p.getId())
                                .nom(p.getName())
                                .sigle(p.getAcronym())
                                .build()));
    }
}
