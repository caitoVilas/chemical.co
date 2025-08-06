package com.ch.userservice.api.exceptions;

import com.ch.core.chcore.exceptions.BadRequestException;
import com.ch.core.chcore.models.ErrorsResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Exception handler for BadRequestException in the Product Service.
 * This class handles exceptions thrown when a request is invalid,
 * returning a standardized error response.
 *
 * @author caito
 */
@RestControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorsResponse> badRequestHandle(BadRequestException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorsResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .timestamp(LocalDateTime.now())
                        .messages(ex.getMessages())
                        .method(request.getMethod())
                        .path(request.getRequestURL().toString())
                        .build()
        );
    }
}
