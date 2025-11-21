package com.portafolio.empresaapi.security.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRoleUpdateDto {

    Set<String> roleNames;

}
