package com.artbite008.spring.basic.event.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AllEventsListener{
        @EventListener
        public void handleEvent(final Object inEvent) {
            System.out.println("- AllEventsListener received event: " + inEvent);
        }
}
