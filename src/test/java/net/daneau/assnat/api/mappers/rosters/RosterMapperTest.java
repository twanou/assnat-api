package net.daneau.assnat.api.mappers.rosters;

import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.api.models.roster.Composition;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;

class RosterMapperTest {

    private final RosterMapper rosterMapper = new RosterMapper();

    @Test
    void toCompositionList() {
        Roster roster = Roster.builder().assignments(List.of(Assignment.builder().partyId("partyId").deputyId("deputyId").districtId("districtId").build())).build();
        DirectoryDTO directoryDTO = DirectoryDTO.builder()
                .circonscriptions(Map.of("districtId", Circonscription.builder().build()))
                .deputies(Map.of("deputyId", Depute.builder().build()))
                .partis(Map.of("partyId", Parti.builder().build()))
                .build();
        List<Composition> compositions = this.rosterMapper.toCompositionList(roster, directoryDTO);
        assertSame(directoryDTO.getDeputies().get("deputyId"), compositions.get(0).getDepute());
        assertSame(directoryDTO.getPartis().get("partyId"), compositions.get(0).getParti());
        assertSame(directoryDTO.getCirconscriptions().get("districtId"), compositions.get(0).getCirconscription());
    }
}