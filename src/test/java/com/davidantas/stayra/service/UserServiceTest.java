package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.ChangePasswordRequest;
import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import com.davidantas.stayra.exception.ResourceNotFoundException;
import com.davidantas.stayra.exception.BadRequestException;
import com.davidantas.stayra.repository.UserRepository;
import com.davidantas.stayra.validation.CreateUserValidationStrategy;
import com.davidantas.stayra.validation.UpdateUserValidationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

class UserServiceTest {

    @Test
    void createsGuestWithEncodedPassword() {
        UserRepository repository = mock(UserRepository.class);
        CreateUserValidationStrategy validation = mock(CreateUserValidationStrategy.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repository, List.of(validation), List.of(), encoder);
        CreateUserRequest request = new CreateUserRequest("david", "David", "david@example.com",
                "Senha@123", "12345678901", LocalDate.of(1995, 5, 20));
        when(encoder.encode("Senha@123")).thenReturn("hash");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = service.create(request);

        assertEquals("david", response.username());
        assertEquals(UserType.GUEST, response.userType());
        assertEquals(UserStatus.ACTIVE, response.status());
        verify(validation).validate(request);
        verify(encoder).encode("Senha@123");
        verify(repository).save(any(User.class));
    }

    @Test
    void findsAuthenticatedUser() {
        UserRepository repository = mock(UserRepository.class);
        UserService service = new UserService(repository, List.of(), List.of(), mock(PasswordEncoder.class));
        User user = new User("@david", "David", "david@example.com", "hash",
                "12345678901", LocalDate.of(1995, 5, 20), UserType.GUEST, UserStatus.ACTIVE);
        when(repository.findByUsername("@david"))
                .thenReturn(Optional.of(user));

        UserResponse response = service.findByUsername("@david");

        assertEquals("@david", response.username());
        assertEquals("david@example.com", response.email());
    }

    @Test
    void throwsWhenAuthenticatedUserDoesNotExist() {
        UserRepository repository = mock(UserRepository.class);
        UserService service = new UserService(
                repository,
                List.of(),
                List.of(),
                mock(PasswordEncoder.class)
        );

        when(repository.findByUsername("@missing"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByUsername("@missing")
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updatesAuthenticatedUserProfile() {
        UserRepository repository = mock(UserRepository.class);
        UpdateUserValidationStrategy validation = mock(UpdateUserValidationStrategy.class);
        UserService service = new UserService(
                repository,
                List.of(),
                List.of(validation),
                mock(PasswordEncoder.class)
        );
        User user = user("David", "david@example.com", LocalDate.of(1995, 5, 20));
        UpdateUserRequest request = new UpdateUserRequest(
                "David Antas",
                "  NEW@EXAMPLE.COM ",
                LocalDate.of(1994, 4, 10)
        );
        when(repository.findByUsername("@david")).thenReturn(Optional.of(user));

        UserResponse response = service.update("@david", request);

        assertEquals("David Antas", response.name());
        assertEquals("new@example.com", response.email());
        assertEquals(LocalDate.of(1994, 4, 10), user.getBirthDate());
        verify(validation).validate(
                eq(user),
                eq(new UpdateUserRequest(
                        "David Antas",
                        "new@example.com",
                        LocalDate.of(1994, 4, 10)
                ))
        );
        verify(repository, never()).save(any());
    }

    @Test
    void partiallyUpdatesAuthenticatedUserProfile() {
        UserRepository repository = mock(UserRepository.class);
        UserService service = new UserService(
                repository,
                List.of(),
                List.of(),
                mock(PasswordEncoder.class)
        );
        User user = user("David", "david@example.com", LocalDate.of(1995, 5, 20));
        when(repository.findByUsername("@david")).thenReturn(Optional.of(user));

        UserResponse response = service.update(
                "@david",
                new UpdateUserRequest("Novo Nome", null, null)
        );

        assertEquals("Novo Nome", response.name());
        assertEquals("david@example.com", response.email());
        assertEquals(LocalDate.of(1995, 5, 20), user.getBirthDate());
    }

    @Test
    void changesAuthenticatedUserPassword() {
        UserRepository repository = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(
                repository,
                List.of(),
                List.of(),
                encoder
        );
        User user = user("David", "david@example.com", LocalDate.of(1995, 5, 20));
        when(repository.findByUsername("@david")).thenReturn(Optional.of(user));
        when(encoder.matches("old-password", "hash")).thenReturn(true);
        when(encoder.matches("new-password", "hash")).thenReturn(false);
        when(encoder.encode("new-password")).thenReturn("new-hash");

        service.changePassword(
                "@david",
                new ChangePasswordRequest("old-password", "new-password")
        );

        assertEquals("new-hash", user.getPassword());
        verify(encoder).matches("old-password", "hash");
        verify(encoder).encode("new-password");
        verify(repository, never()).save(any());
    }

    @Test
    void rejectsInvalidCurrentPassword() {
        UserRepository repository = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repository, List.of(), List.of(), encoder);
        User user = user("David", "david@example.com", LocalDate.of(1995, 5, 20));
        when(repository.findByUsername("@david")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong-password", "hash")).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> service.changePassword(
                        "@david",
                        new ChangePasswordRequest("wrong-password", "new-password")
                )
        );

        verify(encoder, never()).encode(anyString());
    }

    @Test
    void rejectsPasswordEqualToCurrentPassword() {
        UserRepository repository = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repository, List.of(), List.of(), encoder);
        User user = user("David", "david@example.com", LocalDate.of(1995, 5, 20));
        when(repository.findByUsername("@david")).thenReturn(Optional.of(user));
        when(encoder.matches("same-password", "hash")).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.changePassword(
                        "@david",
                        new ChangePasswordRequest("same-password", "same-password")
                )
        );

        verify(encoder, never()).encode(anyString());
    }

    private User user(String name, String email, LocalDate birthDate) {
        return new User(
                "@david",
                name,
                email,
                "hash",
                "12345678901",
                birthDate,
                UserType.GUEST,
                UserStatus.ACTIVE
        );
    }

}
