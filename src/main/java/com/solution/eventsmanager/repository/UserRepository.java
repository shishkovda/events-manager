package com.solution.eventsmanager.repository;

import com.solution.eventsmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByPhoneNumber(String phoneNumber);
    User findById(Long id);
    User findByLogin(String login);
}
