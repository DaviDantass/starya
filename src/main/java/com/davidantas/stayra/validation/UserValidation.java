package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserRepository userRepository;

    public void validate(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already registered");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }

        if (userRepository.existsByCpf(request.cpf())) {
            throw new DuplicateResourceException("CPF already registered");
        }
    }
}
