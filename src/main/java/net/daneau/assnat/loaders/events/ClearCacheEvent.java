package net.daneau.assnat.loaders.events;

import org.springframework.context.ApplicationEvent;

public class ClearCacheEvent extends ApplicationEvent {

    public ClearCacheEvent(Object source) {
        super(source);
    }
}
