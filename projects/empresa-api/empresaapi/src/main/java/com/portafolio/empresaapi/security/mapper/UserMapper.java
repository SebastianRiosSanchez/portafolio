package com.portafolio.empresaapi.security.mapper;

import com.portafolio.empresaapi.security.dto.RoleResponseDto;
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

    public static UserResponseDto userEntityToUserResponseDtoById(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setUserName(entity.getUsername());
        dto.setIsEnable(entity.getIsEnable());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setRoles(
                entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet())
        );
        return dto;
    }

}
