package com.ch.userservice.services.impl;

import com.ch.core.chcore.enums.RoleName;
import com.ch.core.chcore.events.HighMsg;
import com.ch.core.chcore.exceptions.BadRequestException;
import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.helpers.ValidationHelper;
import com.ch.core.chcore.logs.WriteLog;
import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;
import com.ch.userservice.persistence.entities.Role;
import com.ch.userservice.persistence.repositories.RoleRepository;
import com.ch.userservice.persistence.repositories.UserRepository;
import com.ch.userservice.services.contracts.UserService;
import com.ch.userservice.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * UserServiceImpl class implements the UserService interface.
 * It provides methods for creating users and retrieving all users.
 * It includes user validation and password encoding.
 *
 * @author caito
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private  final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, HighMsg> kafkaTemplate;

    /**
     * Creates a new user based on the provided UserRequest.
     * Validates the user details and encodes the password before saving.
     *
     * @param request the UserRequest containing user details
     */
    @Override
    @Transactional
    public void createUser(UserRequest request) {
        log.info(WriteLog.logInfo("--> Creating user service"));
        this.validateUser(request);
        log.info(WriteLog.logInfo("--> User validation passed keeping.."));
        var user = UserMapper.mapToEntity(request);
        Set<Role> roles = new HashSet<>();
        if (request.getRole().equals(RoleName.ROLE_ADMIN)){
            log.info(WriteLog.logInfo("--> User role is ADMIN, assigning admin role"));
            Role adminRole = roleRepository.findByRole(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new NotFoundException("Admin role not found"));
            roles.add(adminRole);
            log.info(WriteLog.logInfo("--> User role is USER, assigning admin role"));
            Role userRole = roleRepository.findByRole(RoleName.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("User role not found"));
            roles.add(userRole);
        } else {
            log.info(WriteLog.logInfo("--> User role is USER, assigning user role"));
            Role userRole = roleRepository.findByRole(RoleName.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("User role not found"));
            roles.add(userRole);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
        log.info(WriteLog.logInfo("--> send message to broker"));
       /* try {
            kafkaTemplate.send("highTopic",
                    HighMsg.builder()
                            .email(user.getEmail())
                            .username(user.getName())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        CompletableFuture<SendResult<String, HighMsg>> future = kafkaTemplate.send(
                "highTopic",
                HighMsg.builder()
                        .email(user.getEmail())
                        .username(user.getName())
                        .build()
        );
        future.whenCompleteAsync((r,t) ->{
            if (t != null) {
                log.error(WriteLog.logError("--> Error sending message to broker: " + t.getMessage()));
                throw new RuntimeException("Error sending message to broker", t);
            } else {
                log.info(WriteLog.logInfo("--> Message sent to broker successfully"));
            }
        });

    }

    /**
     * Retrieves all users from the repository.
     * Maps each UserApp entity to a UserResponse DTO.
     *
     * @return a list of UserResponse DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info(WriteLog.logInfo("--> Retrieving all users"));
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToDto)
                .toList();
    }

    /**
     * Retrieves a user by their ID.
     * Maps the UserApp entity to a UserResponse DTO.
     *
     * @param id the ID of the user to retrieve
     * @return the UserResponse DTO of the retrieved user
     * @throws NotFoundException if the user with the given ID does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info(WriteLog.logInfo("--> Retrieving user service ID: " + id));
        return userRepository.findById(id)
                .map(UserMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> User not found with ID: " + id));
                    return new NotFoundException("User not found with ID: " + id);
                });
    }

    /**
     * Validates the user details from the UserRequest.
     * Checks for required fields and formats, and throws BadRequestException if validation fails.
     *
     * @param request the UserRequest to validate
     */
    private void validateUser(UserRequest request) {
        var errors = new ArrayList<String>();
        log.info(WriteLog.logInfo("--> Validating user service..."));

        if (request.getName() == null || request.getName().isBlank()) {
            errors.add("Name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.add("Email is required");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            errors.add("Email already exists");
        } else if (!ValidationHelper.validateEmail(request.getEmail())) {
            errors.add("Invalid email format");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.add("Password is required");
        } else if (!ValidationHelper.validatePassword(request.getPassword())){
            errors.add("Invalid password format");
        }
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            errors.add("Phone is required");
        }

        if (!errors.isEmpty()) {
            log.error(WriteLog.logError("--> User validation failed: " + String.join(", ", errors)));
            throw new BadRequestException(errors);
        }
    }
}
