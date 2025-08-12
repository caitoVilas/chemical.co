package com.ch.core.chcore.models;

import com.ch.core.chcore.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RoleResponse class represents a response model for user roles.
 * It contains a single field 'role' which indicates the role of the user.
 * This class is used to encapsulate role information in API responses.
 *
 * @author caito
 *
 */
@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class RoleResponse implements Serializable {
    private RoleName role;
}
