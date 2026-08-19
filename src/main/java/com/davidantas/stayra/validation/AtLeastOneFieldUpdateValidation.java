package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class AtLeastOneFieldUpdateValidation implements UpdateUserValidationStrategy {
    @Override
    public void validate(User user, UpdateUserRequest request) {
        if (request.name() == null && request.email() == null && request.birthDate() == null) {
            throw new BadRequestException("At least one field must be provided");
        }
    }
}
