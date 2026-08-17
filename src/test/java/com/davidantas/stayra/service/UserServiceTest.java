package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import com.davidantas.stayra.repository.UserRepository;
import com.davidantas.stayra.validation.UserValidation;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;

class UserServiceTest {

    @Test
    void createsGuestWithEncodedPassword() {
        UserRepository repository = mock(UserRepository.class);
        UserValidation validation = mock(UserValidation.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UserService service = new UserService(repository, validation, encoder);
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
        UserService service = new UserService(repository, mock(UserValidation.class), mock(PasswordEncoder.class));
        User user = new User("@david", "David", "david@example.com", "hash",
                "12345678901", LocalDate.of(1995, 5, 20), UserType.GUEST, UserStatus.ACTIVE);
        when(repository.findByUsernameOrEmail("@david", "@david")).thenReturn(Optional.of(user));

        UserResponse response = service.findByUsername("@david");

        assertEquals("@david", response.username());
        assertEquals("david@example.com", response.email());
    }
}
