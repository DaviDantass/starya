package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueCpfValidation implements CreateUserValidationStrategy {
    private final UserRepository userRepository;

    @Override
    public void validate(CreateUserRequest request) {
        if (userRepository.existsByCpf(request.cpf())) {
            throw new DuplicateResourceException("CPF already registered");
        }
    }
}
