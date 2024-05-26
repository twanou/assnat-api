package net.daneau.assnat.loaders;

import net.daneau.assnat.client.documents.Deputy;
import net.daneau.assnat.client.documents.Roster;
import net.daneau.assnat.client.documents.subdocuments.Assignment;
import net.daneau.assnat.client.repositories.DeputyRepository;
import net.daneau.assnat.client.repositories.RosterRepository;
import net.daneau.assnat.loaders.exceptions.LoadingException;
import net.daneau.assnat.utils.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private RosterRepository rosterRepositoryMock;
    @Mock
    private DeputyRepository deputyRepositoryMock;
    @Mock
    private ErrorHandler errorHandlerMock;
    private DeputyFinder deputyFinder;

    @Test
    void findByCompleteName() {
        Deputy deputyParizeau = Deputy.builder().id("1").title("M.").firstName("Jacques").lastName("Parizeau").build();
        Assignment assignmentParizeau = Assignment.builder().deputyId("1").partyId("2").ridingId("3").build();
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(
                Optional.of(Roster.builder()
                        .assignments(List.of(
                                assignmentParizeau,
                                Assignment.builder().deputyId("4").partyId("5").ridingId("6").build()
                        ))
                        .build()));
        when(deputyRepositoryMock.findAllById(List.of("1", "4"))).thenReturn(List.of(
                deputyParizeau,
                Deputy.builder().id("4").title("M.").firstName("René").lastName("Lévesque").build()
        ));
        this.deputyFinder = new DeputyFinder(rosterRepositoryMock, deputyRepositoryMock, errorHandlerMock);

        Assignment assignment = this.deputyFinder.findByCompleteName("M. Jacques Parizeau");
        verify(errorHandlerMock).assertSize(eq(1), eq(List.of(deputyParizeau)), ArgumentMatchers.<Supplier<LoadingException>>any());
        verify(errorHandlerMock).assertNotNull(same(assignmentParizeau), ArgumentMatchers.<Supplier<LoadingException>>any());
        assertSame(assignmentParizeau, assignment);
    }

    @Test
    void findByCompleteNameNoCache() {
        when(rosterRepositoryMock.findByEndDate(null)).thenReturn(Optional.empty());
        
        this.deputyFinder = new DeputyFinder(rosterRepositoryMock, deputyRepositoryMock, errorHandlerMock);
        assertThrows(LoadingException.class, () -> this.deputyFinder.findByCompleteName("M. Jacques Parizeau"));
    }
}