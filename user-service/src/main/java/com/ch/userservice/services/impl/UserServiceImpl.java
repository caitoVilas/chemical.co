package com.ch.userservice.services.impl;

import com.ch.core.chcore.enums.RoleName;
import com.ch.core.chcore.events.HighMsg;
import com.ch.core.chcore.exceptions.BadRequestException;
import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.helpers.ValidationHelper;
import com.ch.core.chcore.logs.WriteLog;
import com.ch.userservice.api.models.requests.EnableUser;
import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;
import com.ch.userservice.persistence.entities.Role;
import com.ch.userservice.persistence.entities.ValidationToken;
import com.ch.userservice.persistence.repositories.RoleRepository;
import com.ch.userservice.persistence.repositories.UserRepository;
import com.ch.userservice.persistence.repositories.ValidationTokenRepository;
import com.ch.userservice.services.contracts.UserService;
import com.ch.userservice.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final ValidationTokenRepository validationTokenRepository;

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

        log.info(WriteLog.logInfo("--> User role is USER, assigning user role"));
        Role userRole = roleRepository.findByRole(RoleName.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("User role not found"));
            roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);
        log.info(WriteLog.logInfo("--> send message to broker"));
        ValidationToken token = generateValidationToken(user.getEmail());
        CompletableFuture<SendResult<String, HighMsg>> future = kafkaTemplate.send(
                "highTopic",
                HighMsg.builder()
                        .email(user.getEmail())
                        .username(user.getName())
                        .validationToken(token.getToken())
                        .build()
        );
        future.whenCompleteAsync((r,t) ->{
            if (t != null) {
                log.error(WriteLog.logError("--> Error sending message to broker: " + t.getMessage()));
                throw new RuntimeException("Error sending message to broker", t);
            } else {
                validationTokenRepository.save(token);
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
     * Enables a user account based on the provided EnableUser request.
     * Validates the token and updates the user's status to enabled.
     *
     * @param request the EnableUser request containing the token and user details
     * @throws NotFoundException if the validation token or user is not found
     * @throws BadRequestException if the validation token has expired
     */
    @Override
    @Transactional
    public void enableUser(EnableUser request) {
        log.info(WriteLog.logInfo("--> Enabling user service"));
        var token = validationTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> Validation token not found: " + request.getToken()));
                    return new NotFoundException("Validation token not found: " + request.getToken());
                });
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.error(WriteLog.logError("--> Validation token expired for email: " + token.getEmail()));
            throw new BadRequestException(List.of("Validation token expired for email: " + token.getEmail()));
        }
        var user = userRepository.findByEmail(token.getEmail())
                .orElseThrow(() -> {
                    log.error(WriteLog.logError("--> User not found with email: " + token.getEmail()));
                    return new NotFoundException("User not found with email: " + token.getEmail());
                });
        var errors = new ArrayList<String>();
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.add("Password are required");
        }else if (request.getConfirmPassword() == null || request.getConfirmPassword().isBlank()) {
            errors.add("Confirm password is required");
        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.add("Passwords do not match");
        }else if(!ValidationHelper.validatePassword(request.getPassword())){
            errors.add("Invalid password format");
        }
        if (!errors.isEmpty()) {
            log.error(WriteLog.logError("--> User validation failed: " + String.join(", ", errors)));
            throw new BadRequestException(errors);
        }
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        validationTokenRepository.delete(token);
        log.info(WriteLog.logInfo("--> send notification to broker"));
        CompletableFuture<SendResult<String, String>> future = kafkaStringTemplate.send(
                "enableUserTopic", "registration-complete");
        future.whenCompleteAsync((r,t) -> {
            if (t != null) {
                log.error(WriteLog.logError("--> Error sending message to broker: " + t.getMessage()));
                throw new RuntimeException("Error sending message to broker", t);
            } else {
                validationTokenRepository.save(token);
                log.info(WriteLog.logInfo("--> Message sent to broker successfully"));
            }
        });
    }

    /**
     * Sets the role of a user to admin based on their email.
     * Updates the user's roles and saves the changes.
     *
     * @param email the email of the user to set as admin
     * @return the UserResponse DTO of the updated user
     * @throws NotFoundException if the user or roles are not found
     */
    @Override
    public UserResponse setAdmin(String email) {
        log.info(WriteLog.logInfo("--> set role admin to user " + email));
        var user = userRepository.findByEmail(email)
                .orElseThrow(() ->{
                    log.error(WriteLog.logError("--> User not found with email " + email));
                    return new BadRequestException(List.of("User not found with email " + email));
                });
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRole(RoleName.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("User role not found"));
        roles.add(userRole);
        Role adminRole = roleRepository.findByRole(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new NotFoundException("Admin Role mot found"));
        roles.add(adminRole);
        user.setRoles(roles);
        return UserMapper.mapToDto(userRepository.save(user));
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
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            errors.add("Phone is required");
        }

        if (!errors.isEmpty()) {
            log.error(WriteLog.logError("--> User validation failed: " + String.join(", ", errors)));
            throw new BadRequestException(errors);
        }
    }

    /**
     * Generates a validation token for the user.
     * The token is a UUID string and has an expiry date set to 1 day from now.
     *
     * @param email the email of the user for whom the token is generated
     * @return a ValidationToken object containing the token and expiry date
     */
    private ValidationToken generateValidationToken(String email) {
        log.info(WriteLog.logInfo("--> Generating validation Token..."));
        return ValidationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(1)) // Token valid for 1 day
                .email(email)
                .build();
    }
}
