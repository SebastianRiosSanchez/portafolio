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
public class UserResponseDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String userName;
    private String password;
    private Boolean isDeleted;
    private Boolean isEnable;
    private Set<String> roles;


}
