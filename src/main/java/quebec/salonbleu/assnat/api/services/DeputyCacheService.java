package quebec.salonbleu.assnat.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.cache.CacheKey;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DeputyCacheService {

    private final DeputyRepository deputyRepository;

    @Cacheable(CacheKey.Constants.DEPUTIES)
    public Map<UUID, Depute> getDeputies() {
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
