package net.daneau.assnat.api.mappers.rosters;

import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.roster.Composition;
import net.daneau.assnat.client.documents.Roster;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RosterMapper {

    public List<Composition> toCompositionList(Roster roster, DirectoryDTO directoryDTO) {
        return roster.assignments.stream()
                .map(r -> Composition.builder()
                        .parti(directoryDTO.getPartis().get(r.getPartyId()))
                        .depute(directoryDTO.getDeputies().get(r.getDeputyId()))
                        .circonscription(directoryDTO.getCirconscriptions().get(r.getDistrictId())).build())
                .toList();
    }
}
