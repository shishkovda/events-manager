package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.getById(id);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }

}
