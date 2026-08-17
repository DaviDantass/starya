package com.davidantas.stayra.validation;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.exception.DuplicateResourceException;
import com.davidantas.stayra.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserValidationTest {

    private UserRepository repository;
    private UserValidation validation;
    private CreateUserRequest request;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        validation = new UserValidation(repository);
        request = new CreateUserRequest("david", "David", "david@example.com",
                "Senha@123", "12345678901", LocalDate.of(1995, 5, 20));
    }

    @Test
    void acceptsUniqueUser() {
        assertDoesNotThrow(() -> validation.validate(request));
    }

    @Test
    void rejectsDuplicateUsername() {
        when(repository.existsByUsername(request.username())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> validation.validate(request));
        verify(repository, never()).existsByEmail(anyString());
    }

    @Test
    void rejectsDuplicateEmail() {
        when(repository.existsByEmail(request.email())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> validation.validate(request));
        verify(repository, never()).existsByCpf(anyString());
    }

    @Test
    void rejectsDuplicateCpf() {
        when(repository.existsByCpf(request.cpf())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> validation.validate(request));
    }
}
