package com.portafolio.empresaapi.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private Boolean isEnable;
    private Boolean isDeleted;
    private Set<String> roles;
}
