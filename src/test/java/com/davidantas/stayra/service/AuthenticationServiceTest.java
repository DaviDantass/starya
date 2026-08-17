package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.LoginRequest;
import com.davidantas.stayra.dto.LoginResponse;
import com.davidantas.stayra.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Test
    void returnsBearerTokenAfterSuccessfulAuthentication() {
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        Authentication authentication = mock(Authentication.class);
        AuthenticationService service = new AuthenticationService(manager, jwtService);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        LoginResponse response = service.login(new LoginRequest("david", "Senha@123"));

        assertEquals("token", response.token());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void translatesInvalidCredentials() {
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        AuthenticationService service = new AuthenticationService(manager, jwtService);
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("invalid"));

        assertThrows(BadRequestException.class,
                () -> service.login(new LoginRequest("david", "wrong")));
        verifyNoInteractions(jwtService);
    }
}
