package com.davidantas.stayra.service;

import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Test
    void findsByUsernameOrEmail() {
        UserRepository repository = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(repository);
        User user = mock(User.class);
        when(repository.findByUsernameOrEmail("david", "david")).thenReturn(Optional.of(user));

        assertSame(user, service.loadUserByUsername("david"));
    }

    @Test
    void rejectsUnknownIdentifier() {
        UserRepository repository = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(repository);
        when(repository.findByUsernameOrEmail("missing", "missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing"));
    }
}
