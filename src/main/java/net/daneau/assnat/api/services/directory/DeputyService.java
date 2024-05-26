package net.daneau.assnat.api.services.directory;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.repositories.DeputyRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DeputyService {

    private final DeputyRepository deputyRepository;

    Map<String, Depute> getDeputes() {
        return this.deputyRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Deputy::getId,
                        d -> Depute.builder()
                                .id(d.getId())
                                .titre(d.getTitle())
                                .prenom(d.getFirstName())
                                .nom(d.getLastName())
                                .build()));
    }
}
