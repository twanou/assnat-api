package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeputyService {

    private final DeputyRepository deputyRepository;

    public Map<String, Depute> getDeputies() {
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
