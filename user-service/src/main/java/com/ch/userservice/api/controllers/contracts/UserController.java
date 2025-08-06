package com.ch.userservice.api.controllers.contracts;

import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * UserController interface defines the contract for user-related operations.
 * It includes methods for creating a new user and retrieving all users.
 * Each method is annotated with OpenAPI annotations for documentation and security requirements.
 *
 * @author caito
 *
 */
public interface UserController {

    @PostMapping()
    @SecurityRequirement(name = "security token")
    @Operation(description = "Add a new user")
    @Parameter(name = "request", description = "user request object containing details of the user to be added")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "user added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createUser(@RequestBody UserRequest request);

    @GetMapping
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "users retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserResponse>> getUsers();

    @GetMapping("/{id}")
    @SecurityRequirement(name = "security token")
    @Operation(description = "Retrieve all users")
    @Parameter(name = "id", description = "user id to be retrieved")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "users retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No content, no categories found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id);
}
