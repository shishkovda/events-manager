package com.solution.eventsmanager.repository;

import com.solution.eventsmanager.entity.Alias;
import com.solution.eventsmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AliasRepository extends JpaRepository<Alias, UUID> {
    List<Alias> findAll();
}
