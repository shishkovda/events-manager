package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserEventsTemplateService {
    UsersEventsTemplate createUserEventsTemplate(UsersEventsTemplate usersEventsTemplate);
    UsersEventsTemplate getUsersEventsTemplateByEventIdAndUserId(Long eventId, Long userId);
}
