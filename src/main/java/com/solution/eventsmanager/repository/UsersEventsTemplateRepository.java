package com.solution.eventsmanager.repository;

import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersEventsTemplateRepository extends JpaRepository<UsersEventsTemplate, UUID> {
    UsersEventsTemplate getUsersEventsTemplateByEventIdAndUserId(Long eventId, Long userId);
}
