package com.solution.eventsmanager.config;

import com.solution.eventsmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    @Autowired
    public UserService userService(){
        return new UserServiceImpl();
    }

    @Bean
    @Autowired
    public EventService eventService(){
        return new EventServiceImpl();
    }

    @Bean
    @Autowired
    public UserEventsTemplateService userEventsTemplateService(){
        return new UserEventsTemplateServiceImpl();
    }

}
