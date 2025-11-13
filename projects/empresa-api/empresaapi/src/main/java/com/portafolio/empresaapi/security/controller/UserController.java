package com.portafolio.empresaapi.security.controller;

import com.portafolio.empresaapi.security.dto.UserResponseDto;
import com.portafolio.empresaapi.security.dto.UserUpdateRequestDto;
import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param firstName {{@link String}}
     * @param userName  {{@link String}}
     * @param isEnable  {{@link Boolean}}
     * @param pageable  {{@link Pageable}}
     * @return ResponseEntity<Page<UserResponseDto>>
     * @nameMethod getAllUsers
     * @description Method to get all users
     * @autor Sebastian rios
     * @date 11/11/2025
     */
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Boolean isEnable, Pageable pageable
    ) {
        Page<UserResponseDto> users = userService.getAllUsers(firstName, userName, isEnable, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * @param userId {{@link Long}}
     * @return UserResponseDto
     * @nameMethod getUserById
     * @description Method to get a user record by their id
     * @autor Sebastian rios
     * @date 11/11/2025
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * @param request {{@link UserUpdateRequestDto}}
     * @return ResponseEntity<Optional<UserResponseDto>>
     * @nameMethod updateUser
     * @description Method to update a user record
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    @PutMapping()
    public ResponseEntity<Optional<UserResponseDto>> updateUser(@RequestBody UserUpdateRequestDto request) {
        Optional<UserResponseDto> user = userService.updateUser(request);
        return ResponseEntity.ok(user);

    }

    /**
     * @param request {{@link UserUpdateRequestDto}}
     * @return ResponseEntity<Optional<UserResponseDto>>
     * @nameMethod deleteUserById
     * @description Method to set  TRUE isDelete attribute
     * @autor Sebastian rios
     * @date 12/11/2025
     */
    @PostMapping("/delete")
    public ResponseEntity<Optional<UserResponseDto>> deleteUserById(@RequestBody UserUpdateRequestDto request) {
        Optional<UserResponseDto> user = userService.deleteUserById(request.getUserId());
        return ResponseEntity.ok(user);
    }

}
























