package com.ch.core.chcore.models;

import com.ch.core.chcore.enums.RoleName;
import lombok.*;

/**
 * Entity class representing a Role in the system.
 * Implements GrantedAuthority for Spring Security integration.
 *
 * @author caito
 *
 */
@Getter@Setter@Builder
public class Role  {
    private Long id;
    private RoleName role;

}
