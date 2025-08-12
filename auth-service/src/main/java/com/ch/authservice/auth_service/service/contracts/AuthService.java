package com.ch.authservice.auth_service.service.contracts;

import com.ch.authservice.auth_service.api.models.responses.LoginResponse;
import com.ch.core.chcore.models.LoginRequest;

/**
 * AuthService interface defining authentication operations.
 * This service handles user login and token generation.
 * It is responsible for validating user credentials and issuing JWT tokens.
 * The implementation of this interface should ensure secure handling of authentication data.
 *
 * @author caito
 *
 */
public interface AuthService {

    LoginResponse login(LoginRequest request);
}
