package com.ch.userservice.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * VakidationToken represents a token used for email validation in the user service.
 * It contains fields for the token value, expiry date, and associated email.
 *  This entity is mapped to the "vakidation_tokens" table in the database.
 *  The token is used to verify the user's email address during the registration process.
 *
 * @author caito
 *
 */
@Entity
@Table(name = "vakidation_tokens")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@Builder
public class ValidationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expiryDate;
    private String email;
}
