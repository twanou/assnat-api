package quebec.salonbleu.assnat.loaders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quebec.salonbleu.assnat.client.documents.Assignment;
import quebec.salonbleu.assnat.client.documents.Deputy;
import quebec.salonbleu.assnat.client.repositories.AssignmentRepository;
import quebec.salonbleu.assnat.client.repositories.DeputyRepository;
import quebec.salonbleu.assnat.loaders.exceptions.LoadingException;
import quebec.salonbleu.assnat.utils.ErrorHandler;
import test.utils.TestUUID;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeputyFinderTest {

    @Mock
    private AssignmentRepository assignmentRepositoryMock;
    @Mock
    private DeputyRepository deputyRepositoryMock;
    @Mock
    private ErrorHandler errorHandlerMock;
    @InjectMocks
    private DeputyFinder deputyFinder;

    @Test
    void findByCompleteName() {
        Deputy deputyParizeau = Deputy.builder().id(TestUUID.ID1).title("M.").firstName("Jacques").lastName("Parizeau").build();
        Assignment assignmentParizeau = Assignment.builder().deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(
                assignmentParizeau,
                Assignment.builder().deputyId(TestUUID.ID4).partyId(TestUUID.ID5).districtId(TestUUID.ID6).build()));
        when(deputyRepositoryMock.findAllById(List.of(TestUUID.ID4, TestUUID.ID1))).thenReturn(List.of(
                deputyParizeau,
                Deputy.builder().id(TestUUID.ID4).title("M.").firstName("René").lastName("Lévesque").build()
        ));

        Optional<Assignment> assignment = this.deputyFinder.findByCompleteName("M. Jacques Parizeau");
        verify(errorHandlerMock).assertLessThanEquals(eq(1), eq(List.of(deputyParizeau)), ArgumentMatchers.<Supplier<LoadingException>>any());
        verify(errorHandlerMock).assertNotNull(same(assignmentParizeau), ArgumentMatchers.<Supplier<LoadingException>>any());
        assertSame(assignmentParizeau, assignment.orElseThrow());
    }

    @Test
    void findByLastName() {
        Deputy deputyParizeau = Deputy.builder().id(TestUUID.ID1).title("M.").firstName("Jacques").lastName("Parizeau").build();
        Assignment assignmentParizeau = Assignment.builder().deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(
                assignmentParizeau,
                Assignment.builder().deputyId(TestUUID.ID4).partyId(TestUUID.ID5).districtId(TestUUID.ID6).build()));
        when(deputyRepositoryMock.findAllById(List.of(TestUUID.ID4, TestUUID.ID1))).thenReturn(List.of(
                deputyParizeau,
                Deputy.builder().id(TestUUID.ID4).title("M.").firstName("René").lastName("Lévesque").build()
        ));

        Optional<Assignment> assignment = this.deputyFinder.findByLastName("M. Parizeau");
        verify(errorHandlerMock).assertLessThanEquals(eq(1), eq(List.of(deputyParizeau)), ArgumentMatchers.<Supplier<LoadingException>>any());
        verify(errorHandlerMock).assertNotNull(same(assignmentParizeau), ArgumentMatchers.<Supplier<LoadingException>>any());
        assertSame(assignmentParizeau, assignment.orElseThrow());
    }

    @Test
    void findByLastNameAndDistrict() {
        Deputy deputyParizeau = Deputy.builder().id(TestUUID.ID1).title("M.").firstName("Jacques").lastName("Parizeau").lastDistrict("L'Assomption").build();
        Assignment assignmentParizeau = Assignment.builder().deputyId(TestUUID.ID1).partyId(TestUUID.ID2).districtId(TestUUID.ID3).build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(
                assignmentParizeau,
                Assignment.builder().deputyId(TestUUID.ID4).partyId(TestUUID.ID5).districtId(TestUUID.ID6).build()));
        when(deputyRepositoryMock.findAllById(List.of(TestUUID.ID4, TestUUID.ID1))).thenReturn(List.of(
                deputyParizeau,
                Deputy.builder().id(TestUUID.ID4).title("M.").firstName("René").lastName("Lévesque").build()
        ));

        Optional<Assignment> assignment = this.deputyFinder.findByLastNameAndDistrict("M. Parizeau", "L'Assomption");
        verify(errorHandlerMock).assertLessThanEquals(eq(1), eq(List.of(deputyParizeau)), ArgumentMatchers.<Supplier<LoadingException>>any());
        verify(errorHandlerMock).assertNotNull(same(assignmentParizeau), ArgumentMatchers.<Supplier<LoadingException>>any());
        assertSame(assignmentParizeau, assignment.orElseThrow());
    }

    @Test
    void findByCompleteNameNoCache() {
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of());

        this.deputyFinder = new DeputyFinder(assignmentRepositoryMock, deputyRepositoryMock, errorHandlerMock);
        assertThrows(LoadingException.class, () -> this.deputyFinder.findByCompleteName("M. Jacques Parizeau"));
    }
}