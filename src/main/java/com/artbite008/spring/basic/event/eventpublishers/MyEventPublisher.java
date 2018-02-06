package com.artbite008.spring.basic.event.eventpublishers;

import com.artbite008.spring.basic.event.events.BarEvent;
import com.artbite008.spring.basic.event.events.FooEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyEventPublisher implements ApplicationEventPublisherAware {
    protected ApplicationEventPublisher mApplicationEventPublisher;


    @Scheduled(fixedRate = 4000L)
    public void publishFooEvents() {
        final Date theDate = new Date();
        final FooEvent theFooEvent = new FooEvent(this, "Foo event sent at: " + theDate.toString());

        System.out.println("MyEventPublisher - Before publishing FooEvent");
        mApplicationEventPublisher.publishEvent(theFooEvent);
        System.out.println("MyEventPublisher - After publishing FooEvent");
    }

    /**
     * Publishes {@link BarEvent}s at regular interval.
     */
    @Scheduled(fixedRate = 3000L)
    public void publishBarEvents() {
        final Date theDate = new Date();
        final BarEvent theBarEvent = new BarEvent(this, "Bar event sent at: " + theDate.toString());

        System.out.println("MyEventPublisher - Before publishing BarEvent");
        mApplicationEventPublisher.publishEvent(theBarEvent);
        System.out.println("MyEventPublisher - After publishing BarEvent");
    }


    public void setApplicationEventPublisher(final ApplicationEventPublisher inApplicationEventPublisher) {
        mApplicationEventPublisher = inApplicationEventPublisher;
    }
}
