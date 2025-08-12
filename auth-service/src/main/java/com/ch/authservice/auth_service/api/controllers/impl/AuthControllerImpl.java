package com.ch.authservice.auth_service.api.controllers.impl;

import com.ch.authservice.auth_service.api.controllers.contracts.AuthController;
import com.ch.authservice.auth_service.service.contracts.AuthService;
import com.ch.core.chcore.models.LoginRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization API", description = "Controller for authentication operations")
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
