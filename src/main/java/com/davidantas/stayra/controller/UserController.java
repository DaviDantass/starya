package com.davidantas.stayra.controller;

import com.davidantas.stayra.dto.ChangePasswordRequest;
import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }

    @PatchMapping("/me")
    public UserResponse update(
            Authentication authentication,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return userService.update(
                authentication.getName(),
                request
        );
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changePassword(
                authentication.getName(),
                request
        );
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication) {
        userService.delete(authentication.getName());
    }
}
