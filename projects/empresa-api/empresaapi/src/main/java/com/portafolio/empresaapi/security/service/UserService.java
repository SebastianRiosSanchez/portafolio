package com.portafolio.empresaapi.security.service;

import com.portafolio.empresaapi.security.dto.UserResponseDto;
import com.portafolio.empresaapi.security.dto.UserUpdateRequestDto;
import com.portafolio.empresaapi.security.mapper.UserMapper;
import com.portafolio.empresaapi.security.model.RoleEntity;
import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.repository.RoleRepository;
import com.portafolio.empresaapi.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageImpl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository _userRepository, RoleRepository _roleRepository) {
        this.userRepository = _userRepository;
        this.roleRepository = _roleRepository;
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
     * @param request {{@link UserUpdateRequestDto}}
     * @return Boolean
     * @nameMethod validateRequest
     * @description Method to validate data request
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    private Boolean validateRequest(UserUpdateRequestDto request) {
        return StringUtils.hasText(request.getFirstName()) && StringUtils.hasText(request.getLastName());
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

    /**
     * @param request {{@link UserUpdateRequestDto}}
     * @return Optional<UserResponseDto>
     * @nameMethod updateUser
     * @description Method to update a user record
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    public Optional<UserResponseDto> updateUser(UserUpdateRequestDto request) {
        if (this.userRepository.findById(request.getUserId()).isEmpty()) {
            throw new RuntimeException("El usuario con el id: ".concat(request.getUserId().toString()).concat(" no se encontro "));
        }
        if (validateRequest(request)) {
            UserEntity userFound = userRepository.findById(request.getUserId()).get();
            UserEntity updatedUser = setData(userFound, request);
            return Optional.of(UserMapper.userEntityToUserResponseDto(userRepository.save(updatedUser)));
        }
        return Optional.empty();
    }

    /**
     * @param userFound {{@link UserEntity}}
     * @param request   {{@link UserUpdateRequestDto}}
     * @return {@link UserEntity}
     * @nameMethod setData
     * @description Method to set data to UserEntity record found
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    private UserEntity setData(UserEntity userFound, UserUpdateRequestDto request) {
        userFound.setFirstname(request.getFirstName());
        userFound.setLastname(request.getLastName());

        Set<RoleEntity> newRoles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("No existe un rol con ese nombre")))
                .collect(Collectors.toSet());
        userFound.setRoles(newRoles);

        return userFound;

    }

}
