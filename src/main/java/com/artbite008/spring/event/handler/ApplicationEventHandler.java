package com.artbite008.spring.event.handler;

import java.util.List;
import java.util.Map;

import com.artbite008.spring.event.common.IEventProcessor;
import com.artbite008.spring.event.domain.DurableEvent;
import com.artbite008.spring.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class ApplicationEventHandler {
	
	private Logger logger = LoggerFactory.getLogger(ApplicationEventHandler.class);
	
	//@Resource(name="EventProcessorMap")
	private Map<String, List<IEventProcessor>> processorMap;

	/**
	 * 
	 * do event processing in a new thread
	 * 
	 * in future version, may use background processor to pick up events from database then do actions
	 * 
	 */
	@Async
	@EventListener
	public void handleDurableEvent(DurableEvent event) {
		logger.debug("processing durable event {}", event);

		//process event
		if(processorMap !=  null) {
				List<IEventProcessor> processors = processorMap.get(buildKey(event));
				if(processors == null) {
					processors = processorMap.get("*");
				}
				if(processors != null) {
					for(IEventProcessor p : processors) {
						try {
							if(p.support(event)) {
								p.processEvent(event);
							}
						} catch (Exception ex) {
								//propagate exception
								//throw ex;
						}
					}
				}
			
		}
		
	}
	
	private String buildKey(Event event) {
		return event.getSourceType() + "-" + event.getEventType();
	}
	
	
	public Map<String, List<IEventProcessor>> getProcessorMap() {
		return processorMap;
	}

	public void setProcessorMap(Map<String, List<IEventProcessor>> processorMap) {
		this.processorMap = processorMap;
	}

}
