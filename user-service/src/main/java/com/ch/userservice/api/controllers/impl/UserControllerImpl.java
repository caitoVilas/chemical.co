package com.ch.userservice.api.controllers.impl;

import com.ch.core.chcore.logs.WriteLog;
import com.ch.userservice.api.controllers.contracts.UserController;
import com.ch.userservice.api.models.requests.EnableUser;
import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;
import com.ch.userservice.services.contracts.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserControllerImpl class implements the UserController interface.
 * It provides endpoints for creating users and retrieving all users.
 * It uses the UserService to handle business logic.
 *
 * @author caito
 *
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "User management operations")
@Slf4j
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public ResponseEntity<?> createUser(UserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.warn(WriteLog.logWarning("--> No users found"));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<?> enableUser(EnableUser request) {
        userService.enableUser(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserResponse> setAdmin(String email) {
        return ResponseEntity.ok(userService.setAdmin(email));
    }
}
