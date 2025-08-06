package com.ch.userservice.persistence.repositories;

import com.ch.core.chcore.enums.RoleName;
import com.ch.userservice.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes a method to find a Role by its RoleName.
 *
 * @author caito
 *
 */
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(RoleName name);
}
