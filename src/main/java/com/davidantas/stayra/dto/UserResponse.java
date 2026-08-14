package com.davidantas.stayra.dto;

import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String name,
        String email,
        UserType userType,
        UserStatus status,
        LocalDateTime createdAt
){
}
