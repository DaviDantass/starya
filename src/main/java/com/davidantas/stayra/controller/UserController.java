package com.davidantas.stayra.controller;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request);
    }
}
