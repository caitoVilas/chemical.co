package com.ch.userservice.persistence.repositories;

import com.ch.userservice.persistence.entities.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ValidationTokenRepository is a Spring Data JPA repository interface for managing
 * VakidationToken entities. It provides methods to perform CRUD operations and
 * custom queries related to validation tokens.
 *
 * @author caito
 *
 */
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Integer> {
    Optional<ValidationToken> findByToken(String token);
}
