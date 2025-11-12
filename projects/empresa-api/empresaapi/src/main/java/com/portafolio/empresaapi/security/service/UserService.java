package com.portafolio.empresaapi.security.service;

import com.portafolio.empresaapi.security.dto.UserResponseDto;
import com.portafolio.empresaapi.security.mapper.UserMapper;
import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param firstName {{@link String}}
     * @param userName  {{@link String}}
     * @param isEnable  {{@link Boolean}}
     * @param pageable  {{@link Pageable}}
     * @return Page<UserResponseDto>
     * @nameMethod getAllUsers
     * @description Method to get all users
     * @autor Sebastian rios
     * @date 11/11/2025
     */
    public Page<UserResponseDto> getAllUsers(String firstName, String userName, Boolean isEnable, Pageable pageable) {
        Page<UserEntity> usersPage;
        if (StringUtils.hasText(firstName) || StringUtils.hasText(userName) || isEnable != null) {
            usersPage = userRepository.findAllByFilters(firstName, userName, isEnable, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        return new PageImpl<>(
                usersPage.getContent().stream()
                        .map(UserMapper::userEntityToUserResponseDto)
                        .collect(Collectors.toList()),
                pageable,
                usersPage.getTotalElements()
        );
    }

    /**
     * @param id {{@link Long}}
     * @return UserResponseDto
     * @nameMethod getUserById
     * @description Method to get a user record by their id
     * @autor Sebastian rios
     * @date 11/11/2025
     */
    public UserResponseDto getUserById(Long id) {
        UserEntity userFound = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id ".concat(String.valueOf(id).concat(" no encontrado"))));

        return UserMapper.userEntityToUserResponseDto(userFound);

    }

}
