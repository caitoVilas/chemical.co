package com.ch.authservice.auth_service.service.impl;

import com.ch.authservice.auth_service.api.models.responses.LoginResponse;
import com.ch.authservice.auth_service.configs.security.JwtTokenProvider;
import com.ch.authservice.auth_service.service.contracts.AuthService;
import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.exceptions.UnauthorizedException;
import com.ch.core.chcore.logs.WriteLog;
import com.ch.core.chcore.models.LoginRequest;
import com.ch.core.chcore.models.UserAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;



    @Override
    public LoginResponse login(LoginRequest request) {
        log.info(WriteLog.logInfo("--> authService - login - start"));
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpRequest re = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:10300/chco/api/v1/users/full-data/" + request.getEmail()))
                    .header("content-type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(re, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                log.error(WriteLog.logError("--> authService - login - user not found"));
                throw new NotFoundException("User not found");
            }
            UserAuthResponse user = mapper.readValue(response.body(), UserAuthResponse.class);
            this.authenticate(user, request);
            return LoginResponse.builder()
                    .access_token(jwtTokenProvider.generateToken(user))
                    .build();
        } catch (IOException | InterruptedException e) {
            log.error(WriteLog.logError("Error during login: " + e.getMessage()));
        }

        return null;
    }

    private void authenticate(UserAuthResponse user, LoginRequest request) {
        log.info(WriteLog.logInfo("--> authService - authenticate - start"));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error(WriteLog.logError("--> Password not match"));
            throw new UnauthorizedException("Incorrect Password");
        }

        if (!user.isEnabled()) {
            log.error(WriteLog.logError("--> User not enabled"));
            throw new UnauthorizedException("User not enabled");
        }
        if (!user.isAccountNonExpired()) {
            log.error(WriteLog.logError("--> User account expired"));
            throw new UnauthorizedException("User account expired");
        }
        if (!user.isAccountNonLocked()) {
            log.error(WriteLog.logError("--> User account locked"));
            throw new UnauthorizedException("User account locked");
        }

        if (!user.isCredentialsNonExpired()){
            log.error(WriteLog.logError("--> User credentials expired"));
            throw new UnauthorizedException("User credentials expired");
        }
    }
}
