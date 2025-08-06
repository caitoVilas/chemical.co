package com.ch.userservice.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for security-related beans.
 * Provides a PasswordEncoder bean for encoding passwords.
 *
 * @author caito
 *
 */
@Configuration
public class BeansConfig {

    /**
     * Bean for encoding passwords using BCrypt.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
