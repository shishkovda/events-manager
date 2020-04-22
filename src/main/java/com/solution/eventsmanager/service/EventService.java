package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    Event updateEvent(Event event);
    List<Event> getEventsByTitle(String title);
}
