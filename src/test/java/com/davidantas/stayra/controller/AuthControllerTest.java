package com.davidantas.stayra.controller;

import com.davidantas.stayra.dto.LoginRequest;
import com.davidantas.stayra.dto.LoginResponse;
import com.davidantas.stayra.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void delegatesLoginToAuthenticationService() {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        AuthController controller = new AuthController(authenticationService);
        LoginRequest request = new LoginRequest("@david", "Senha@123");
        LoginResponse expected = new LoginResponse("token", "Bearer", 3600L);
        when(authenticationService.login(request)).thenReturn(expected);

        ResponseEntity<LoginResponse> response = controller.login(request);

        assertSame(expected, response.getBody());
        verify(authenticationService).login(request);
    }
}
