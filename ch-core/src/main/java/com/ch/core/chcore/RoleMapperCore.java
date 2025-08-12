package com.ch.core.chcore;

import com.ch.core.chcore.models.Role;
import com.ch.core.chcore.models.RoleResponse;

public class RoleMapperCore {

    public static RoleResponse mapToDto(Role role){
        return RoleResponse.builder()
                .role(role.getRole())
                .build();
    }


}
