package com.davidantas.stayra.service;

import com.davidantas.stayra.dto.CreateUserRequest;
import com.davidantas.stayra.dto.UserResponse;
import com.davidantas.stayra.entity.User;
import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import com.davidantas.stayra.repository.UserRepository;
import com.davidantas.stayra.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(CreateUserRequest createUserRequest) {
        userValidation.validate(createUserRequest);

        User user = new User(
                createUserRequest.username(),
                createUserRequest.name(),
                createUserRequest.email(),
                passwordEncoder.encode(createUserRequest.password()),
                createUserRequest.cpf(),
                createUserRequest.birthDate(),
                UserType.GUEST,
                UserStatus.ACTIVE
        );
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getUserType(),
                savedUser.getStatus(),
                savedUser.getCreatedAt()
        );
    }

    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserResponse(user.getId(), user.getUsername(), user.getName(),
                user.getEmail(), user.getUserType(), user.getStatus(), user.getCreatedAt());
    }
}
