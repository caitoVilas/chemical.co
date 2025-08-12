package com.ch.userservice.configs.security.filters;

import com.ch.core.chcore.logs.WriteLog;
import com.ch.core.chcore.models.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JwtEntryPoint is responsible for handling unauthorized access attempts
 * by returning a structured error response.
 *
 * @author caito
 *
 */
@Component
@Slf4j
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException
                                                        authException) throws IOException, ServletException {
        final String MSG = "Unauthorized resource access";
        var res = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(MSG)
                .method(request.getMethod())
                .path(request.getRequestURL().toString())
                .build();
        log.error(WriteLog.logError("--> " + MSG));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String apiError = mapper.writeValueAsString(res);
        response.getWriter().write(apiError);
    }
}
