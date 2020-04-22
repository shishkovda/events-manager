package com.solution.eventsmanager.service;

import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByPhoneNUmber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUSerById(Long id) {
        return userRepository.findById(id);
    }
}
