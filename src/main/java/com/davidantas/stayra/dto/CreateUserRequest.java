package com.davidantas.stayra.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank
        @Size(min = 3, max = 30)
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
