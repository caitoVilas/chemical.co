package com.ch.userservice.persistence.repositories;

import com.ch.userservice.persistence.entities.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * Repository interface for UserApp entity.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 *
 * @author caito
 *
 */
public interface UserRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserApp> findAllByNameContainingIgnoreCase(String name);
}
