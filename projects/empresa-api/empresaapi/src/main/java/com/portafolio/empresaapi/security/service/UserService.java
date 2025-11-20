package com.portafolio.empresaapi.security.service;

import com.portafolio.empresaapi.exception.InvalidRequestException;
import com.portafolio.empresaapi.exception.RoleNotFoundException;
import com.portafolio.empresaapi.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException(id));

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
        UserEntity foundUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        if (!validateRequest(request)) {
            throw new InvalidRequestException("Los datos ingresados no son validos");
        }
        UserEntity updatedUser = setData(foundUser, request);
        return Optional.of(UserMapper.userEntityToUserResponseDto(updatedUser));

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
                        .orElseThrow(() -> new RoleNotFoundException(roleName)))
                .collect(Collectors.toSet());
        userFound.setRoles(newRoles);

        return userFound;

    }

    /**
     * @param userId {{@link Long}}
     * @return {@link Optional<UserResponseDto>}
     * @nameMethod deleteUserById
     * @description Method to set TRUE isDelete attribute
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    public Optional<UserResponseDto> deleteUserById(Long userId) {
        if (this.userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("No se ha encontrado un usuario con el id: ".concat(userId.toString()));
        }
        Optional<UserEntity> foundUser = userRepository.findById(userId);
        foundUser.ifPresent(user -> {
            user.setIsDeleted(Boolean.TRUE);
            user.setIsEnable(Boolean.FALSE);
        });
        userRepository.save(foundUser.get());
        return foundUser.map(UserMapper::userEntityToUserResponseDto);
    }

    /**
     * @param userId {{@link Long}}
     * @return {@link Optional<UserResponseDto>}
     * @nameMethod activateUser
     * @description Method to set TRUE isEnable attribute
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    public Optional<UserResponseDto> activateUser(Long userId) {
        if (this.userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("No se ha encontrado un usuario con id: .".concat(userId.toString()));
        }
        Optional<UserEntity> foundUser = userRepository.findById(userId);
        if (foundUser.get().getIsDeleted().equals(Boolean.TRUE)) {
            throw new RuntimeException("El usuario con el id: ".concat(userId.toString()).concat("ha sido eliminado"));
        }
        foundUser.ifPresent(user -> {
            user.setIsEnable(Boolean.FALSE);
        });
        userRepository.save(foundUser.get());
        return foundUser.map(UserMapper::userEntityToUserResponseDto);
    }

}
