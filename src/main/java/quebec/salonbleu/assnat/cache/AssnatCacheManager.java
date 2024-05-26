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
        this.clear(CacheKey.CURRENT_ASSIGNMENTS, CacheKey.ALL_ASSIGNMENTS);
        this.eventBus.publishEvent(new ClearCacheEvent(this));
    }

    public void clearSubjectCaches() {
        this.clear(CacheKey.LAST_UPDATE, CacheKey.NEXT_UPDATE);
    }

    private void clear(CacheKey... key) {
        Stream.of(key)
                .map(cacheKey -> this.cacheManager.getCache(cacheKey.name()))
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }
}
