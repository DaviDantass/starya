package com.davidantas.stayra.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank
        @Pattern(regexp = "^@[a-zA-Z0-9_]{2,29}$")
        String username,

        @NotBlank
        @Size(min = 2, max = 100)
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Pattern(regexp = "^\\d{11}$")
        String cpf,

        @NotNull
        @Past
        LocalDate birthDate
) {
}
