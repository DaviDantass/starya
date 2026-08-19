package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.entity.User;

public interface UpdateUserValidationStrategy {
    void validate(User user, UpdateUserRequest request);
}
