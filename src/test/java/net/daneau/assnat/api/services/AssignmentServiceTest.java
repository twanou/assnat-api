package net.daneau.assnat.api.services;

import net.daneau.assnat.api.models.commons.Affectation;
import net.daneau.assnat.api.models.commons.Circonscription;
import net.daneau.assnat.api.models.commons.Depute;
import net.daneau.assnat.api.models.commons.Parti;
import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.utils.PhotoUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private DeputyService deputyServiceMock;
    @Mock
    private DistrictService districtServiceMock;
    @Mock
    private PartyService partyServiceMock;
    @Mock
    private PhotoUtils photoUtilsMock;
    @Mock
    private AssignmentRepository assignmentRepositoryMock;
    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    void getCurrentAssignments() {
        Map<String, Depute> deputeMap = Map.of("1", Depute.builder().prenom("prenom").nom("nom").build());
        Map<String, Circonscription> circonscriptionMap = Map.of("2", Circonscription.builder().nom("circonscription").build());
        Map<String, Parti> partiMap = Map.of("3", Parti.builder().nom("parti").build());
        when(deputyServiceMock.getDeputies()).thenReturn(deputeMap);
        when(districtServiceMock.getDistricts()).thenReturn(circonscriptionMap);
        when(partyServiceMock.getParties()).thenReturn(partiMap);
        when(photoUtilsMock.getPhotoUrl("prenom", "nom", "circonscription", "parti")).thenReturn("url");
        Assignment assignment = Assignment.builder().id("0").deputyId("1").districtId("2").partyId("3").build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(assignment));

        Collection<Affectation> response = this.assignmentService.getCurrentAssignments();
        assertEquals(deputeMap.get("1").getId(), response.stream().findFirst().get().getDepute().getId());
        assertEquals(circonscriptionMap.get("2").getId(), response.stream().findFirst().get().getCirconscription().getId());
        assertEquals(partiMap.get("3").getId(), response.stream().findFirst().get().getParti().getId());
        assertEquals("url", response.stream().findFirst().get().getPhotoUrl());
    }

    @Test
    void getAllAssignments() {
        Map<String, Depute> deputeMap = Map.of("1", Depute.builder().prenom("prenom").nom("nom").build());
        Map<String, Circonscription> circonscriptionMap = Map.of("2", Circonscription.builder().nom("circonscription").build());
        Map<String, Parti> partiMap = Map.of("3", Parti.builder().nom("parti").build());
        when(deputyServiceMock.getDeputies()).thenReturn(deputeMap);
        when(districtServiceMock.getDistricts()).thenReturn(circonscriptionMap);
        when(partyServiceMock.getParties()).thenReturn(partiMap);
        when(photoUtilsMock.getPhotoUrl("prenom", "nom", "circonscription", "parti")).thenReturn("url");
        Assignment assignment = Assignment.builder().id("0").deputyId("1").districtId("2").partyId("3").functions(List.of("depute")).build();
        when(assignmentRepositoryMock.findAll()).thenReturn(List.of(assignment));

        Map<String, Affectation> response = this.assignmentService.getAllAssignments();
        assertEquals(deputeMap.get("1").getId(), response.get("0").getDepute().getId());
        assertEquals(circonscriptionMap.get("2").getId(), response.get("0").getCirconscription().getId());
        assertEquals(partiMap.get("3").getId(), response.get("0").getParti().getId());
        assertEquals(assignment.getFunctions().get(0), response.get("0").getFonctions().get(0));
        assertEquals("url", response.get("0").getPhotoUrl());
    }
}