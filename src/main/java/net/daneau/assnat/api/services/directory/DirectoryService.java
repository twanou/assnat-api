package net.daneau.assnat.api.services.directory;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DeputyService deputyService;
    private final DistrictService districtService;
    private final PartyService partyService;

    @Cacheable("directoryCache")
    public DirectoryDTO getDirectory() {
        return DirectoryDTO.builder()
                .deputies(this.deputyService.getDeputes())
                .partis(this.partyService.getParties())
                .circonscriptions(this.districtService.getCirconscriptions())
                .build();
    }
}
