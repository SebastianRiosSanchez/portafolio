package com.portafolio.empresaapi.security.controller;

import com.portafolio.empresaapi.security.dto.UserResponseDto;
import com.portafolio.empresaapi.security.dto.UserRoleUpdateDto;
import com.portafolio.empresaapi.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param userId  {{@link Long}}
     * @param request {{@link UserRoleUpdateDto}}
     * @return {@link ResponseEntity<UserResponseDto>}
     * @nameMethod updateUserRolesByAdmin
     * @description Method to update a user roles
     * @autor Sebastian rios
     * @date 20/11/2025
     */
    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserRolesByAdmin(
            @PathVariable Long userId,
            @RequestBody UserRoleUpdateDto request
    ) {
        UserResponseDto updatedUserRoles = userService.updateUserRolesByAdmin(userId, request.getRoleNames());

        return ResponseEntity.ok(updatedUserRoles);
    }

}
