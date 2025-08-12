package com.ch.userservice.services.contracts;

import com.ch.core.chcore.models.LoginRequest;
import com.ch.core.chcore.models.UserAuthResponse;
import com.ch.userservice.api.models.requests.EnableUser;
import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;

import java.util.List;

/**
 * UserService interface defines the contract for user-related operations.
 * It includes methods for creating a user and retrieving all users.
 * This interface is intended to be implemented by classes that provide the actual business logic for user management.
 *
 * @author caito
 *
 */
public interface UserService {

    void createUser(UserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    void enableUser(EnableUser request);
    UserResponse setAdmin(String email);
    UserAuthResponse getAllDataUser(String email);
}
