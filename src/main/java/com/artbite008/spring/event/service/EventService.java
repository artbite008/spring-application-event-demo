package com.artbite008.spring.event.service;

import java.util.List;
import java.util.Map;

import com.artbite008.spring.event.domain.DurableEvent;
import com.artbite008.spring.event.dao.IEventDao;
import com.artbite008.spring.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
	
	private Logger logger = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private IEventDao eventDao;

	@Transactional
	public void publishDurableEvent(DurableEvent event) {
		//save event
		eventDao.insert(event);
		logger.debug("durable event persisted {}", event);
		
		//create event context
//		Map<String, Object> context = new HashMap<String, Object>();
//		context.put(Event.CONTEXT_AUTH, SecurityContextHolder.getContext().getAuthentication());
//		event.setContext(context);
		
		//publish event
		eventPublisher.publishEvent(event);
		logger.debug("durable event published in application context {}", event);
	}

	public void publishEvent(Event event) {
		//publish event
		eventPublisher.publishEvent(event);
		logger.debug("event published in application context {}", event);
	}

	public List<Event> queryEvents(Map<String, Object> queryParameters, Integer page, Integer size) {
		if(page == null && size == null) {
			return eventDao.findByProperties(queryParameters);
		} else {
			Pageable pageRequest = new PageRequest(page, size, Direction.DESC, "createdTime");
			return eventDao.findByProperties(pageRequest, queryParameters).getContent();
		}
	}

}
