package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtLeastOneFieldUpdateValidationTest {
    private final AtLeastOneFieldUpdateValidation validation =
            new AtLeastOneFieldUpdateValidation();

    @Test
    void acceptsRequestWithAtLeastOneField() {
        UpdateUserRequest request = new UpdateUserRequest("David", null, null);

        assertDoesNotThrow(() -> validation.validate(null, request));
    }

    @Test
    void rejectsRequestWithoutFields() {
        UpdateUserRequest request = new UpdateUserRequest(null, null, null);

        assertThrows(
                BadRequestException.class,
                () -> validation.validate(null, request)
        );
    }
}
