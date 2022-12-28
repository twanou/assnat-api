package net.daneau.assnat.loaders.events;

import org.springframework.context.ApplicationEvent;

public class RosterUpdateEvent extends ApplicationEvent {

    public RosterUpdateEvent(Object source) {
        super(source);
    }
}
