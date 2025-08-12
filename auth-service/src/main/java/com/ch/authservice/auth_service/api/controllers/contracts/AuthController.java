package com.ch.authservice.auth_service.api.controllers.contracts;


import com.ch.core.chcore.models.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    @PostMapping("/login")
    @Operation(summary = "Login user",
            description = "This endpoint allows a user to log in by providing their email and password. " +
                    "It returns a JWT token if the login is successful.")
    @Parameter(name = "request", description = "Login request containing email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials"),
            @ApiResponse(responseCode = "404", description = "Not found, user does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error occurred")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request);

}
