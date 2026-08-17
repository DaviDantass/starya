package com.davidantas.stayra.service;

import com.davidantas.stayra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier)
            throws UsernameNotFoundException {

        return userRepository
                .findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado"
                ));
    }
}
