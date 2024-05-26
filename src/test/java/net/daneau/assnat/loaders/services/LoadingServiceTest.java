package net.daneau.assnat.loaders.services;

import net.daneau.assnat.loaders.assignments.AssignmentLoader;
import net.daneau.assnat.loaders.subjects.LogEntriesSubjectLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadingServiceTest {

    @Mock
    private AssignmentLoader assignmentLoaderMock;
    @Mock
    private LogEntriesSubjectLoader subjectLoaderMock;
    @Mock
    private Clock clockMock;

    private LoadingService loadingService;

    @BeforeEach
    void setup() {
        doAnswer((invocationOnMock) -> {
            ((Runnable) invocationOnMock.getArguments()[0]).run();
            return null;
        }).when(subjectLoaderMock).load(any(Runnable.class));
        this.loadingService = new LoadingService(assignmentLoaderMock, subjectLoaderMock, clockMock, Duration.ofSeconds(3600));
    }

    @Test
    void load() {
        when(clockMock.instant()).thenReturn(Clock.fixed(Instant.ofEpochSecond(3600), ZoneId.systemDefault()).instant());
        this.loadingService.load();
        verify(subjectLoaderMock).load(any(Runnable.class));
        verify(assignmentLoaderMock).load();

        clearInvocations(subjectLoaderMock, assignmentLoaderMock);
        when(clockMock.instant()).thenReturn(Clock.fixed(Instant.ofEpochSecond(6000), ZoneId.systemDefault()).instant());
        this.loadingService.load();
        verify(subjectLoaderMock, never()).load(any(Runnable.class));
        verify(assignmentLoaderMock, never()).load();

        when(clockMock.instant()).thenReturn(Clock.fixed(Instant.ofEpochSecond(10000), ZoneId.systemDefault()).instant());
        this.loadingService.load();
        verify(subjectLoaderMock).load(any(Runnable.class));
        verify(assignmentLoaderMock).load();
    }
}