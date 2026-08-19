package com.davidantas.stayra.controller;

import com.davidantas.stayra.dto.ChangePasswordRequest;
import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController controller;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new UserController(userService);
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("@david");
    }

    @Test
    void delegatesCreateToUserService() {
        CreateUserRequest request = new CreateUserRequest(
                "@david", "David", "david@example.com", "Senha@123", "12345678901",
                LocalDate.of(1990, 1, 1));
        UserResponse expected = mock(UserResponse.class);
        when(userService.create(request)).thenReturn(expected);

        assertSame(expected, controller.create(request));
        verify(userService).create(request);
    }

    @Test
    void delegatesAuthenticatedUserOperationsToUserService() {
        UserResponse expected = mock(UserResponse.class);
        UpdateUserRequest updateRequest = new UpdateUserRequest("David Antas", null, null);
        ChangePasswordRequest passwordRequest = new ChangePasswordRequest("Senha@123", "NovaSenha@123");
        when(userService.findByUsername("@david")).thenReturn(expected);
        when(userService.update("@david", updateRequest)).thenReturn(expected);

        assertSame(expected, controller.me(authentication));
        assertSame(expected, controller.update(authentication, updateRequest));
        controller.changePassword(authentication, passwordRequest);
        controller.delete(authentication);

        verify(userService).findByUsername("@david");
        verify(userService).update("@david", updateRequest);
        verify(userService).changePassword("@david", passwordRequest);
        verify(userService).delete("@david");
    }
}
