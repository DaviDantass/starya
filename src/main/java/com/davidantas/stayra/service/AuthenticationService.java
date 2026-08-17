package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.LoginRequest;
import com.davidantas.stayra.dto.LoginResponse;
import com.davidantas.stayra.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.identifier(),
                                    loginRequest.password()
                            )
                    );

            String token = jwtService.generateToken(authentication);

            return new LoginResponse(
                    token,
                    "Bearer",
                    jwtService.getExpirationSeconds()
            );

        } catch (BadCredentialsException exception) {
            throw new BadRequestException("Credenciais inválidas");
        }
    }
}
