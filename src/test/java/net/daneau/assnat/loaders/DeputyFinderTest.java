package net.daneau.assnat.loaders;

import net.daneau.assnat.client.documents.Assignment;
import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.repositories.AssignmentRepository;
import net.daneau.assnat.client.repositories.DeputyRepository;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.utils.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        Deputy deputyParizeau = Deputy.builder().id("1").title("M.").firstName("Jacques").lastName("Parizeau").photo("photo").build();
        Assignment assignmentParizeau = Assignment.builder().deputyId("1").partyId("2").districtId("3").build();
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of(
                assignmentParizeau,
                Assignment.builder().deputyId("4").partyId("5").districtId("6").build()));
        when(deputyRepositoryMock.findAllById(List.of("1", "4"))).thenReturn(List.of(
                deputyParizeau,
                Deputy.builder().id("4").title("M.").firstName("René").lastName("Lévesque").build()
        ));

        Assignment assignment = this.deputyFinder.findByCompleteName("M. Jacques Parizeau");
        verify(errorHandlerMock).assertSize(eq(1), eq(List.of(deputyParizeau)), ArgumentMatchers.<Supplier<LoadingException>>any());
        verify(errorHandlerMock).assertNotNull(same(assignmentParizeau), ArgumentMatchers.<Supplier<LoadingException>>any());
        assertSame(assignmentParizeau, assignment);
    }

    @Test
    void findByCompleteNameNoCache() {
        when(assignmentRepositoryMock.findByEndDate(null)).thenReturn(List.of());

        this.deputyFinder = new DeputyFinder(assignmentRepositoryMock, deputyRepositoryMock, errorHandlerMock);
        assertThrows(LoadingException.class, () -> this.deputyFinder.findByCompleteName("M. Jacques Parizeau"));
    }
}