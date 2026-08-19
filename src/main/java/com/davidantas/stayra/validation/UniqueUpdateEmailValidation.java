package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUpdateEmailValidation implements UpdateUserValidationStrategy {
    private final UserRepository userRepository;

    @Override
    public void validate(User user, UpdateUserRequest request) {
        if (request.email() != null
                && userRepository.existsByEmailAndIdNot(request.email(), user.getId())) {
            throw new DuplicateResourceException("Email already registered");
        }
    }
}
