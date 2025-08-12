package com.ch.core.chcore.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * UserAuthResponse class represents the response model for user authentication operations.
 * It contains user details such as id, name, email, password, phone, creation and update timestamps,
 * and a set of roles associated with the user.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class UserAuthResponse implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Set<RoleResponse> role;
}
