package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    void updateUser(User user);
    User getUserByPhoneNUmber(String phoneNumber);
    User getUSerById(Long id);
    User getUserByLogin(String login);
}
