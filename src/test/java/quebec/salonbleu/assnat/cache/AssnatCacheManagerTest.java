package quebec.salonbleu.assnat.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import quebec.salonbleu.assnat.loaders.events.ClearCacheEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssnatCacheManagerTest {

    @Mock
    private CacheManager cacheManagerMock;
    @Mock
    private ApplicationEventPublisher applicationEventPublisherMock;
    @Mock
    private Cache cacheMock;
    @InjectMocks
    private AssnatCacheManager assnatCacheManager;

    @Test
    void clearAssignmentCaches() {
        when(cacheManagerMock.getCache(CacheKey.ALL_ASSIGNMENTS.name())).thenReturn(null);
        when(cacheManagerMock.getCache(CacheKey.CURRENT_ASSIGNMENTS.name())).thenReturn(cacheMock);
        this.assnatCacheManager.clearAssignmentCaches();
        verify(cacheMock).clear();
        verify(applicationEventPublisherMock).publishEvent(any(ClearCacheEvent.class));
    }

    @Test
    void clearSubjectCaches() {
        when(cacheManagerMock.getCache(CacheKey.LAST_UPDATE.name())).thenReturn(cacheMock);
        this.assnatCacheManager.clearSubjectCaches();
        verify(cacheMock).clear();
        verify(applicationEventPublisherMock, never()).publishEvent(any());
    }
}