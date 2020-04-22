package com.solution.eventsmanager.repository;

import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Event getById(Long id);
    List<Event> findAllByTitle(String title);
}
