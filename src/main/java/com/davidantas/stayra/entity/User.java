package com.davidantas.stayra.entity;

import com.davidantas.stayra.entity.enums.UserStatus;
import com.davidantas.stayra.entity.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_cpf", columnNames = "cpf")
        }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^@[a-zA-Z0-9_]{2,29}$")
    @Column(nullable = false, length = 30)
    private String username;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255)
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password_hash", nullable = false)
    private String password;

    @NotBlank(message = "CPF is required")
    @Pattern(
            regexp = "^\\d{11}$",
            message = "CPF must contain exactly 11 digits"
    )
    @Column(nullable = false, length = 11)
    private String cpf;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull(message = "User type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @NotNull(message = "User status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public User(
            String username,
            String name,
            String email,
            String password,
            String cpf,
            LocalDate birthDate,
            UserType userType,
            UserStatus status
    ) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.userType = userType;
        this.status = status;
    }

    // Spring security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userType == UserType.HOST) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_GUEST"),
                    new SimpleGrantedAuthority("ROLE_HOST")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_GUEST"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}
