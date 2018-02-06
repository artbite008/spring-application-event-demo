package com.artbite008.spring.basic.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringEventsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringEventsApplication.class, args);

        try {
            Thread.sleep(10000L);
        } catch (final InterruptedException theException) {
        }
    }
}
