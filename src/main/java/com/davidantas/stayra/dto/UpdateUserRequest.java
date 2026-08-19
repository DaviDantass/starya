package com.davidantas.stayra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(

        @Size(min = 2, max = 100)
        String name,

        @Email
        @Size(max = 255)
        String email,

        @Past
        LocalDate birthDate
) {
}
