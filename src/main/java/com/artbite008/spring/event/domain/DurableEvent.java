package com.artbite008.spring.event.domain;

import com.artbite008.spring.event.domain.Event;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="EVENT")
public class DurableEvent extends Event {
	
	public DurableEvent() {
		//default constructor
	}

	public DurableEvent(String originator, String sourceType, String sourceRef, String eventType) {
		super(originator, sourceType, sourceRef, eventType);
	}

}
