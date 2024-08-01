package quebec.salonbleu.assnat.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.api.models.commons.Affectation;
import quebec.salonbleu.assnat.api.models.commons.Circonscription;
import quebec.salonbleu.assnat.api.models.commons.Depute;
import quebec.salonbleu.assnat.api.models.commons.Parti;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.utils.PhotoUtils;
import test.utils.TestUUID;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        Map<UUID, Depute> deputeMap = Map.of(TestUUID.ID1, Depute.builder().prenom("prenom").nom("nom").build());
        Map<UUID, Circonscription> circonscriptionMap = Map.of(TestUUID.ID2, Circonscription.builder().nom("circonscription").build());
        Map<UUID, Parti> partiMap = Map.of(TestUUID.ID3, Parti.builder().nom("parti").build());
        when(deputyServiceMock.getDeputies()).thenReturn(deputeMap);
        when(districtServiceMock.getDistricts()).thenReturn(circonscriptionMap);
        when(partyServiceMock.getParties()).thenReturn(partiMap);
        when(photoUtilsMock.getPhotoUrl("prenom", "nom", "circonscription")).thenReturn("url");
        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).districtId(TestUUID.ID2).partyId(TestUUID.ID3).build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(assignment));

        Collection<Affectation> response = this.assignmentService.getCurrentAssignments();
        assertEquals(deputeMap.get(TestUUID.ID1).getId(), response.stream().findFirst().orElseThrow().getDepute().getId());
        assertEquals(circonscriptionMap.get(TestUUID.ID2).getId(), response.stream().findFirst().orElseThrow().getCirconscription().getId());
        assertEquals(partiMap.get(TestUUID.ID3).getId(), response.stream().findFirst().orElseThrow().getParti().getId());
        assertEquals("url", response.stream().findFirst().orElseThrow().getPhotoUrl());
    }

    @Test
    void getAllAssignments() {
        Map<UUID, Depute> deputeMap = Map.of(TestUUID.ID1, Depute.builder().prenom("prenom").nom("nom").build());
        Map<UUID, Circonscription> circonscriptionMap = Map.of(TestUUID.ID2, Circonscription.builder().nom("circonscription").build());
        Map<UUID, Parti> partiMap = Map.of(TestUUID.ID3, Parti.builder().nom("parti").build());
        when(deputyServiceMock.getDeputies()).thenReturn(deputeMap);
        when(districtServiceMock.getDistricts()).thenReturn(circonscriptionMap);
        when(partyServiceMock.getParties()).thenReturn(partiMap);
        when(photoUtilsMock.getPhotoUrl("prenom", "nom", "circonscription")).thenReturn("url");
        Assignment assignment = Assignment.builder().id(TestUUID.ID4).deputyId(TestUUID.ID1).districtId(TestUUID.ID2).partyId(TestUUID.ID3).functions(List.of("depute")).build();
        when(assignmentRepositoryMock.findAll()).thenReturn(List.of(assignment));

        Map<UUID, Affectation> response = this.assignmentService.getAllAssignments();
        assertEquals(deputeMap.get(TestUUID.ID1).getId(), response.get(TestUUID.ID4).getDepute().getId());
        assertEquals(circonscriptionMap.get(TestUUID.ID2).getId(), response.get(TestUUID.ID4).getCirconscription().getId());
        assertEquals(partiMap.get(TestUUID.ID3).getId(), response.get(TestUUID.ID4).getParti().getId());
        assertEquals(assignment.getFunctions().getFirst(), response.get(TestUUID.ID4).getFonctions().getFirst());
        assertEquals("url", response.get(TestUUID.ID4).getPhotoUrl());
    }
}
