package com.artbite008.spring.listeners;

import com.artbite008.spring.events.BarEvent;
import com.artbite008.spring.events.FooEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FooBarEventListener {
    @EventListener
    public void handleFooEvents(final FooEvent inFooEvent) {
        System.out.println("FooBarEventListener received a FooEvent with the message: " + inFooEvent.getFooMessage());
    }


    @EventListener({BarEvent.class})
    public void handleBarEvents(final BarEvent inBarEvent) {
        System.out.println("FooBarEventListener received a BarEvent with the message: " + inBarEvent.getBarMessage());
    }
}
