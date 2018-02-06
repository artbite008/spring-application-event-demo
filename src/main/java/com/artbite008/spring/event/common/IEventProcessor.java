package com.artbite008.spring.event.common;

import com.artbite008.spring.event.domain.Event;

public interface IEventProcessor {

    void processEvent(Event event);

    boolean support(Event event);
}
