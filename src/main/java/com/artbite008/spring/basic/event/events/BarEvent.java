package com.artbite008.spring.basic.event.events;

import org.springframework.context.ApplicationEvent;

public class BarEvent extends ApplicationEvent {
    protected String mBarMessage;
    public BarEvent(final Object inSource, final String inBarMessage) {
        super(inSource);
        mBarMessage = inBarMessage;
    }

    public String getBarMessage() {
        return mBarMessage;
    }
}
