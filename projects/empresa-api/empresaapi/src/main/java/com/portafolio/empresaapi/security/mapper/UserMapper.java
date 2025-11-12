package com.portafolio.empresaapi.security.mapper;

import com.portafolio.empresaapi.security.dto.UserResponseDto;
import com.portafolio.empresaapi.security.model.RoleEntity;
import com.portafolio.empresaapi.security.model.UserEntity;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponseDto userEntityToUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .userName(userEntity.getUsername())
                .isDeleted(userEntity.getIsDeleted())
                .isEnable(userEntity.getIsEnable())
                .roles(userEntity.getRoles() != null ?
                        userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet())
                        : null)
                .build();
    }

}
