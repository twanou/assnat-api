package net.daneau.assnat.loaders.events;

import org.springframework.context.ApplicationEvent;

public class AssignmentUpdateEvent extends ApplicationEvent {

    public AssignmentUpdateEvent(Object source) {
        super(source);
    }
}
