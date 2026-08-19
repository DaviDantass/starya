package com.davidantas.stayra.config;

import com.davidantas.stayra.service.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter(jwtService, userDetailsService);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesActiveUser() throws Exception {
        UserDetails user = User.withUsername("@david")
                .password("hash")
                .roles("GUEST")
                .build();
        configureValidToken(user);

        filter.doFilter(
                requestWithToken(),
                new MockHttpServletResponse(),
                filterChain
        );

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void rejectsDisabledUser() throws Exception {
        UserDetails user = User.withUsername("@david")
                .password("hash")
                .disabled(true)
                .roles("GUEST")
                .build();
        configureValidToken(user);

        filter.doFilter(
                requestWithToken(),
                new MockHttpServletResponse(),
                filterChain
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void rejectsLockedUser() throws Exception {
        UserDetails user = User.withUsername("@david")
                .password("hash")
                .accountLocked(true)
                .roles("GUEST")
                .build();
        configureValidToken(user);

        filter.doFilter(
                requestWithToken(),
                new MockHttpServletResponse(),
                filterChain
        );

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private void configureValidToken(UserDetails user) {
        when(jwtService.isValid("token")).thenReturn(true);
        when(jwtService.extractUsername("token")).thenReturn("@david");
        when(userDetailsService.loadUserByUsername("@david")).thenReturn(user);
    }

    private MockHttpServletRequest requestWithToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        return request;
    }
}
