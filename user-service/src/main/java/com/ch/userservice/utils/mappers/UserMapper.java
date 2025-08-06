package com.ch.userservice.utils.mappers;

import com.ch.userservice.api.models.requests.UserRequest;
import com.ch.userservice.api.models.responses.UserResponse;
import com.ch.userservice.persistence.entities.UserApp;

import java.util.stream.Collectors;

/**
 * UserMapper class provides utility methods to map between UserApp entities and UserResponse DTOs.
 * It contains methods to convert a UserRequest to a UserApp entity and a UserApp entity to a UserResponse DTO.
 *
 * @author caito
 *
 */
public class UserMapper {

    /**
     * Maps a UserRequest to a UserApp entity.
     *
     * @param request the UserRequest to be mapped
     * @return a UserApp entity with the specified details
     */
    public static UserApp mapToEntity(UserRequest request){
        return UserApp.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    /**
     * Maps a UserApp entity to a UserResponse DTO.
     *
     * @param user the UserApp entity to be mapped
     * @return a UserResponse DTO containing the user's details
     */
    public static UserResponse mapToDto(UserApp user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(user.getRoles().stream()
                        .map(RoleMapper::mapToDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
