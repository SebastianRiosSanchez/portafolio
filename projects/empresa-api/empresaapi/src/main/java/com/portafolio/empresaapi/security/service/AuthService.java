package com.portafolio.empresaapi.security.service;

import com.portafolio.empresaapi.security.auth.AuthenticationRequest;
import com.portafolio.empresaapi.security.auth.AuthenticationResponse;
import com.portafolio.empresaapi.security.auth.RegisterRequest;
import com.portafolio.empresaapi.security.jwt.JwtService;
import com.portafolio.empresaapi.security.model.RoleEntity;
import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.repository.RoleRepository;
import com.portafolio.empresaapi.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (authentication.isAuthenticated()) {
            var userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.generateToken(userDetails);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    /**
     * Registrar un nuevo usuario en el sistema.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUserName(request.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario con el correo ".concat(request.getEmail()).concat(" ya existe"));
        }
        // Asignar rol por defecto (ej. USER)
        RoleEntity defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("El rol ingresado no existe"));

        // Construir entidad del usuario
        UserEntity user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .userName(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isDeleted(false)
                .isEnable(true)
                .build();

        // Asignar el rol
        user.getRoles().add(defaultRole);
        // Guardar datos del usuario
        userRepository.save(user);
        // Generar el token JWT
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();

    }

    /**
     * Autenticar un usuario y generar el JWT.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Autenticar usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Credenciales Invalidas");
        }

        // Cargar el usuario desde la base de datos
        UserEntity user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        String token = jwtService.generateToken((UserDetails) user);


        return AuthenticationResponse.builder()
                .token(token)
                .build();

    }

}
