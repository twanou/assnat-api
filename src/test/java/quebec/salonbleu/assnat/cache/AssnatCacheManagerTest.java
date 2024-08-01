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
    @Mock
    private Cache cacheMock2;
    @Mock
    private Cache cacheMock3;
    @Mock
    private Cache cacheMock4;
    @Mock
    private Cache cacheMock5;
    @InjectMocks
    private AssnatCacheManager assnatCacheManager;

    @Test
    void clearAssignmentCaches() {
        when(cacheManagerMock.getCache(CacheKey.ALL_ASSIGNMENTS.name())).thenReturn(cacheMock);
        when(cacheManagerMock.getCache(CacheKey.CURRENT_ASSIGNMENTS.name())).thenReturn(cacheMock2);
        when(cacheManagerMock.getCache(CacheKey.DEPUTIES.name())).thenReturn(cacheMock3);
        when(cacheManagerMock.getCache(CacheKey.DISTRICTS.name())).thenReturn(cacheMock4);
        when(cacheManagerMock.getCache(CacheKey.PARTIES.name())).thenReturn(cacheMock5);

        this.assnatCacheManager.clearAssignmentCaches();
        verify(cacheMock).clear();
        verify(cacheMock2).clear();
        verify(cacheMock3).clear();
        verify(cacheMock4).clear();
        verify(cacheMock5).clear();
        verify(applicationEventPublisherMock).publishEvent(any(ClearCacheEvent.class));
    }

    @Test
    void clearLastUpdateCache() {
        when(cacheManagerMock.getCache(CacheKey.LAST_UPDATE.name())).thenReturn(cacheMock);

        this.assnatCacheManager.clearLastUpdateCache();
        verify(cacheMock).clear();
        verify(applicationEventPublisherMock, never()).publishEvent(any());
    }

    @Test
    void clearUpcomingLogCaches() {
        when(cacheManagerMock.getCache(CacheKey.NEXT_UPDATE.name())).thenReturn(cacheMock);
        when(cacheManagerMock.getCache(CacheKey.CURRENTLY_LOADING.name())).thenReturn(cacheMock2);

        this.assnatCacheManager.clearUpcomingLogCaches();
        verify(cacheMock).clear();
        verify(cacheMock2).clear();
        verify(applicationEventPublisherMock, never()).publishEvent(any());
    }
}