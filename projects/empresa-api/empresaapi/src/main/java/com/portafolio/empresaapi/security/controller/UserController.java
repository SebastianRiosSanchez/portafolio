package com.portafolio.empresaapi.security.controller;

import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Boolean isEnable, Pageable pageable
    ) {
        Page<UserEntity> users = userService.getAllUsers(firstName, userName, isEnable, pageable);
        return ResponseEntity.ok(users);
    }

}
