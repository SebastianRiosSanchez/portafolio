package com.portafolio.empresaapi.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("Usuario con ID: " + userId + " No encontrado.");
    }

}
