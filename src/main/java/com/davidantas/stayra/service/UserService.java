package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.ChangePasswordRequest;
import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UpdateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import com.davidantas.stayra.exception.BadRequestException;
import com.davidantas.stayra.exception.ResourceNotFoundException;
import com.davidantas.stayra.repository.UserRepository;
import com.davidantas.stayra.validation.CreateUserValidationStrategy;
import com.davidantas.stayra.validation.UpdateUserValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final List<CreateUserValidationStrategy> createValidations;
    private final List<UpdateUserValidationStrategy> updateValidations;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(CreateUserRequest createUserRequest) {
        createValidations.forEach(validation -> validation.validate(createUserRequest));

        User user = new User(createUserRequest.username(),
                createUserRequest.name(),
                createUserRequest.email(),
                passwordEncoder.encode(createUserRequest.password()),
                createUserRequest.cpf(),
                createUserRequest.birthDate(),
                UserType.GUEST, UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    private User findAuthenticatedUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse findByUsername(String username) {
        return toResponse(findAuthenticatedUser(username));
    }

    @Transactional
    public UserResponse update(String username, UpdateUserRequest request) {
        User user = findAuthenticatedUser(username);

        String normalizedEmail = request.email() == null ? null : request.email().trim().toLowerCase();
        UpdateUserRequest normalizedRequest = new UpdateUserRequest(
                request.name(),
                normalizedEmail,
                request.birthDate()
        );

        updateValidations.forEach(validation -> validation.validate(user, normalizedRequest));

        user.updateProfile(
                normalizedRequest.name(),
                normalizedRequest.email(),
                normalizedRequest.birthDate()
        );

        return toResponse(user);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request
    ) {
        User user = findAuthenticatedUser(username);

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPassword()
        )) {
            throw new BadRequestException(
                    "Current password is invalid"
            );
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPassword()
        )) {
            throw new BadRequestException(
                    "New password must be different"
            );
        }

        user.changePassword(passwordEncoder.encode(request.newPassword()));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getUserType(), user.getStatus(), user.getCreatedAt());
    }

}
