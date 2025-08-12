package com.ch.authservice.auth_service.api.exceptions;

import com.ch.core.chcore.exceptions.NotFoundException;
import com.ch.core.chcore.models.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Handles NotFoundException and returns a standardized error response.
 * This class is annotated with @RestControllerAdvice
 * to handle exceptions globally across all controllers.
 *
 * @author caito
 *
 */
@RestControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException e, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .method(request.getMethod())
                        .path(request.getRequestURL().toString())
                .build());
    }
}
