package net.daneau.assnat.api.services;

import net.daneau.assnat.api.mappers.rosters.RosterMapper;
import net.daneau.assnat.api.models.commons.DirectoryDTO;
import net.daneau.assnat.api.models.roster.Composition;
import net.daneau.assnat.api.services.directory.DirectoryService;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.repositories.RosterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RosterServiceTest {

    @Mock
    private RosterMapper rosterMapperMock;
    @Mock
    private DirectoryService directoryServiceMock;
    @Mock
    private RosterRepository rosterRepositoryMock;
    @InjectMocks
    private RosterService rosterService;
    
    @Test
    void getCurrentRoster() {
        List<Composition> compositions = List.of(Composition.builder().build());
        DirectoryDTO directoryDTO = DirectoryDTO.builder().build();
        Roster roster = Roster.builder().build();
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(Optional.of(roster));
        when(directoryServiceMock.getDirectory()).thenReturn(directoryDTO);
        when(rosterMapperMock.toCompositionList(same(roster), same(directoryDTO))).thenReturn(compositions);

        List<Composition> response = this.rosterService.getCurrentRoster();
        assertEquals(compositions, response);
    }

    @Test
    void getCurrentRosterNotFound() {
        when(directoryServiceMock.getDirectory()).thenReturn(DirectoryDTO.builder().build());
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> this.rosterService.getCurrentRoster());
    }
}