package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;

public interface CreateUserValidationStrategy {
    void validate(CreateUserRequest request);
}
