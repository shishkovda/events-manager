package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.repository.UserRepository;
import com.solution.eventsmanager.repository.UsersEventsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserEventsTemplateServiceImpl implements UserEventsTemplateService {

    @Autowired
    UsersEventsTemplateRepository usersEventsTemplateRepository;

    @Override
    public UsersEventsTemplate createUserEventsTemplate(UsersEventsTemplate usersEventsTemplate) {
        return usersEventsTemplateRepository.save(usersEventsTemplate);
    }

    @Override
    public UsersEventsTemplate getUsersEventsTemplateByEventIdAndUserId(Long eventId, Long userId) {
        return usersEventsTemplateRepository.getUsersEventsTemplateByEventIdAndUserId(eventId, userId);
    }
}