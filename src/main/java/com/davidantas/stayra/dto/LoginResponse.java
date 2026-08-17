package com.davidantas.stayra.dto;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn
) {
}
