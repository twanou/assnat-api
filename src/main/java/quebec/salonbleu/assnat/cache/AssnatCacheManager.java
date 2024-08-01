package quebec.salonbleu.assnat.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import quebec.salonbleu.assnat.loaders.events.ClearCacheEvent;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AssnatCacheManager {

    private final CacheManager cacheManager;
    private final ApplicationEventPublisher eventBus;

    public void clearAssignmentCaches() {
        this.clear(CacheKey.CURRENT_ASSIGNMENTS,
                CacheKey.ALL_ASSIGNMENTS,
                CacheKey.DEPUTIES,
                CacheKey.PARTIES,
                CacheKey.DISTRICTS);
        this.eventBus.publishEvent(new ClearCacheEvent(this));
    }

    public void clearUpcomingLogCaches() {
        this.clear(CacheKey.NEXT_UPDATE, CacheKey.CURRENTLY_LOADING);
    }

    public void clearLastUpdateCache() {
        this.clear(CacheKey.LAST_UPDATE);
    }

    private void clear(CacheKey... key) {
        Stream.of(key)
                .map(cacheKey -> this.cacheManager.getCache(cacheKey.name()))
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }
}
