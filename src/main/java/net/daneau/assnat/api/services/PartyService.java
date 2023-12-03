package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.client.documents.Party;
import net.daneau.assnat.client.repositories.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    public Map<String, Parti> getParties() {
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
