package net.daneau.assnat.api.services;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.api.mappers.rosters.RosterMapper;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.roster.Composition;
import net.daneau.assnat.api.services.directory.DirectoryService;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.repositories.RosterRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterMapper rosterMapper;
    private final DirectoryService directoryService;
    private final RosterRepository rosterRepository;

    @Cacheable("rosterCache")
    public List<Composition> getCurrentRoster() {
        DirectoryDTO directoryDTO = this.directoryService.getDirectory();
        Roster currentRoster = this.rosterRepository.findByEndDate(null).orElseThrow();
        return this.rosterMapper.toCompositionList(currentRoster, directoryDTO);
    }
}
