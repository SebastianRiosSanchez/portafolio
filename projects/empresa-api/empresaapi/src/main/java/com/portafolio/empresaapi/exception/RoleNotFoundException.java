package com.portafolio.empresaapi.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("No existe un rol con el nombre: " + roleName);
    }

}
