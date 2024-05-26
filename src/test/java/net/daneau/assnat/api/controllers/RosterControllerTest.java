package net.daneau.assnat.api.controllers;

import net.daneau.assnat.api.models.roster.Composition;
import net.daneau.assnat.api.models.roster.responses.CompositionReponse;
import net.daneau.assnat.api.services.RosterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RosterControllerTest {

    @Mock
    private RosterService rosterServiceMock;
    @InjectMocks
    private RosterController rosterController;

    @Test
    void getCurrentRoster() {
        List<Composition> compositions = List.of(Composition.builder().build());
        when(rosterServiceMock.getCurrentRoster()).thenReturn(compositions);
        CompositionReponse response = this.rosterController.getCurrentRoster();
        assertEquals(compositions, response.getCompositions());
    }
}