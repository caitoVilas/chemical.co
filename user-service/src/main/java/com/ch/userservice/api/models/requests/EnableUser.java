package com.ch.userservice.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * EnableUser class represents a request to enable a user account.
 * It contains fields for the user's email, token, password, and confirmPassword.
 * This class is used to encapsulate the data required for enabling a user account.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class EnableUser implements Serializable{
    private String token;
    private String password;
    private String confirmPassword;
}
