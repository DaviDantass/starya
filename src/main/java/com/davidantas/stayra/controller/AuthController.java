package com.davidantas.stayra.controller;

import com.davidantas.stayra.dto.LoginRequest;
import com.davidantas.stayra.dto.LoginResponse;
import com.davidantas.stayra.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
