package net.daneau.assnat.cache;

import lombok.RequiredArgsConstructor;
import net.daneau.assnat.loaders.events.ClearCacheEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AssnatCacheManager {

    private final CacheManager cacheManager;
    private final ApplicationEventPublisher eventBus;

    public void clearAllCaches() {
        Arrays.stream(CacheKey.values())
                .map(cacheKey -> this.cacheManager.getCache(cacheKey.name()))
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
        this.eventBus.publishEvent(new ClearCacheEvent(this));
    }
}
