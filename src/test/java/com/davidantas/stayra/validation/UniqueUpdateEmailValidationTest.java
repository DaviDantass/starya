package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UniqueUpdateEmailValidationTest {
    private final UserRepository repository = mock(UserRepository.class);
    private final UniqueUpdateEmailValidation validation =
            new UniqueUpdateEmailValidation(repository);

    @Test
    void acceptsAvailableEmail() {
        User user = user();
        UpdateUserRequest request = new UpdateUserRequest(null, "new@example.com", null);

        assertDoesNotThrow(() -> validation.validate(user, request));
    }

    @Test
    void ignoresEmailWhenItWasNotProvided() {
        validation.validate(user(), new UpdateUserRequest("David", null, null));

        verifyNoInteractions(repository);
    }

    @Test
    void rejectsEmailOwnedByAnotherUser() {
        User user = user();
        UpdateUserRequest request = new UpdateUserRequest(null, "used@example.com", null);
        when(repository.existsByEmailAndIdNot("used@example.com", user.getId()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> validation.validate(user, request)
        );
    }

    private User user() {
        return new User(
                "@david",
                "David",
                "david@example.com",
                "hash",
                "12345678901",
                LocalDate.of(1995, 5, 20),
                UserType.GUEST,
                UserStatus.ACTIVE
        );
    }
}
