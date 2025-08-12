package com.ch.authservice.auth_service.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class to define security-related beans.
 * This class provides a PasswordEncoder bean that uses BCrypt hashing.
 *
 * @author caito
 *
 */
@Configuration
public class BeansConfig {

    /**
     * Provides a PasswordEncoder bean that uses BCrypt hashing.
     *
     * @return a PasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
