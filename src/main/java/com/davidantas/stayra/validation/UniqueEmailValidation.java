package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidation implements CreateUserValidationStrategy {
    private final UserRepository userRepository;

    @Override
    public void validate(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }
    }
}
