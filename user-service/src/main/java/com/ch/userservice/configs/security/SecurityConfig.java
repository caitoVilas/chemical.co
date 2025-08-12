package com.ch.userservice.configs.security;

import com.ch.userservice.configs.security.filters.JwtEntryPoint;
import com.ch.userservice.configs.security.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig class configures the security settings for the application.
 * It defines the security filter chain and specifies that all requests are permitted.
 * CSRF protection is disabled, and session management is set to stateless.
 *
 * @author caito
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;

    /**
     * Configures the security filter chain for the application.
     * It allows all requests, disables CSRF protection, and sets session management to stateless.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/swagger-ui/**",
                                                 "/v3/api-docs/**",
                                                 "/v1/users/create").permitAll()
                                .requestMatchers("/v1/users/enable-user/**").permitAll()
                                .requestMatchers("/v1/users/full-data/**").permitAll()
                                .requestMatchers("/v1/users/enable-admin/**").hasRole("ADMIN")
                                .requestMatchers("/v1/users/remove/**").hasRole("ADMIN")
                              .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
