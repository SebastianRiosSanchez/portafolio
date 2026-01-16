package com.portafolio.empresaapi.security.config;

import com.portafolio.empresaapi.security.model.RoleEntity;
import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.repository.RoleRepository;
import com.portafolio.empresaapi.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        // ✅ Crear roles si no existen
        RoleEntity userRole = createRoleIfNotExist("USER", "Default user role");
        RoleEntity adminRole = createRoleIfNotExist("ADMIN", "Administrator role");
        RoleEntity managerRole = createRoleIfNotExist("MANAGER", "Manager role");

        // ✅ Crear usuario superadmin si no existe
        userRepository.findByUserName("superadmin").orElseGet(() -> {
            UserEntity superAdmin = UserEntity.builder()
                    .firstname("Super")
                    .lastname("Admin")
                    .userName("superadmin")
                    .password(passwordEncoder.encode("admin123"))
                    .isDeleted(false)
                    .roles(new HashSet<>(Set.of(adminRole, userRole)))
                    .build();

            return userRepository.save(superAdmin);
        });
    }

    private RoleEntity createRoleIfNotExist(String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    RoleEntity role = RoleEntity.builder()
                            .name(name)
                            .description(description)
                            .build();
                    return roleRepository.save(role);
                });
    }
}
