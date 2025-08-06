package com.ch.userservice.utils.mappers;

import com.ch.core.chcore.enums.RoleName;
import com.ch.userservice.api.models.responses.RoleResponse;
import com.ch.userservice.persistence.entities.Role;

/**
 * RoleMapper class provides utility methods to map between Role entities and RoleResponse DTOs.
 * It contains methods to convert a RoleName enum to a Role entity and a Role entity to a RoleResponse DTO.
 *
 * @author caito
 *
 */
public class RoleMapper {

    /**
     * Maps a RoleName enum to a Role entity.
     *
     * @param name the RoleName enum to be mapped
     * @return a Role entity with the specified role name
     */
    public static Role mapToEntity(RoleName name){
        return Role.builder()
                .role(RoleName.valueOf(name.name()))
                .build();
    }

    /**
     * Maps a Role entity to a RoleResponse DTO.
     *
     * @param role the Role entity to be mapped
     * @return a RoleResponse DTO containing the role name
     */
    public static RoleResponse mapToDto(Role role){
        return RoleResponse.builder()
                .role(role.getRole())
                .build();
    }
}
