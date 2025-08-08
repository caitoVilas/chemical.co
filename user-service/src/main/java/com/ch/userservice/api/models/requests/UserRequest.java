package com.ch.userservice.api.models.requests;

import com.ch.core.chcore.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserRequest class represents the request model for user creation.
 * It contains fields for user details such as name, email, password, phone, and role.
 * This class is used to transfer data from the client to the server.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class UserRequest implements Serializable {
    private String name;
    private String email;
    private String phone;
}
