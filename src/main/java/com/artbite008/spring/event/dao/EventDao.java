package com.artbite008.spring.event.dao;

import com.artbite008.spring.event.domain.Event;
import org.springframework.stereotype.Service;

@Service
public class EventDao extends JpaDomainDao<Event, String> implements IEventDao {
}
