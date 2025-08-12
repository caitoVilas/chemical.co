package com.ch.userservice.api.exceptions;

import com.ch.core.chcore.exceptions.TokenException;
import com.ch.core.chcore.models.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Handles TokenException and returns a standardized error response.
 * This class is annotated with @RestControllerAdvice to handle exceptions globally
 * across all controllers in the application.
 *
 * @author caito
 *
 */
@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(TokenException.class)
    protected ResponseEntity<ErrorResponse> tokenExceptionHandler(TokenException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .timestamp(LocalDateTime.now())
                        .method(request.getMethod())
                        .path(request.getRequestURL().toString())
                .build());
    }
}
