package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateUserValidationStrategiesTest {
    private UserRepository repository;
    private CreateUserRequest request;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        request = new CreateUserRequest(
                "@david",
                "David",
                "david@example.com",
                "Senha@123",
                "12345678901",
                LocalDate.of(1995, 5, 20)
        );
    }

    @Test
    void usernameValidationAcceptsAvailableUsername() {
        assertDoesNotThrow(() -> new UniqueUsernameValidation(repository).validate(request));
    }

    @Test
    void usernameValidationRejectsDuplicateUsername() {
        when(repository.existsByUsername(request.username())).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> new UniqueUsernameValidation(repository).validate(request)
        );
    }

    @Test
    void emailValidationRejectsDuplicateEmail() {
        when(repository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> new UniqueEmailValidation(repository).validate(request)
        );
    }

    @Test
    void cpfValidationRejectsDuplicateCpf() {
        when(repository.existsByCpf(request.cpf())).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> new UniqueCpfValidation(repository).validate(request)
        );
    }
}
