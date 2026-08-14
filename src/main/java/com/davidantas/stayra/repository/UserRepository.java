package com.davidantas.stayra.repository;

import com.davidantas.stayra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
