package com.portafolio.empresaapi.security.service;

import com.portafolio.empresaapi.security.model.UserEntity;
import com.portafolio.empresaapi.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserEntity> getAllUsers(String firstName, String userName, Boolean isEnable, Pageable pageable) {
        if (StringUtils.hasText(firstName) || StringUtils.hasText(userName) || isEnable != null) {
            return userRepository.findAllByFilters(firstName, userName, isEnable, pageable);
        }

        return userRepository.findAll(pageable);

    }

}
