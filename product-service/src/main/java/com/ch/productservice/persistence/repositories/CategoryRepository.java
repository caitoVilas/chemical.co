package com.ch.productservice.persistence.repositories;

import com.ch.productservice.persistence.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Category entities.
 * Provides methods to perform CRUD operations and custom queries.
 *
 * @author caito
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}
