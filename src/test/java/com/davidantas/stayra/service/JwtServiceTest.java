package com.davidantas.stayra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new JwtService();
        setField("secret", "WStirHuaJfTs6TxIOXcUPagDej9qoQZ5nn87JXzDFC0");
        setField("expirationSeconds", 3600L);
    }

    @Test
    void generatesAndReadsValidToken() {
        User user = new User("david", "hash", List.of());
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String token = service.generateToken(authentication);

        assertTrue(service.isValid(token));
        assertEquals("david", service.extractUsername(token));
        assertEquals(3600L, service.getExpirationSeconds());
    }

    @Test
    void rejectsMalformedToken() {
        assertFalse(service.isValid("not-a-jwt"));
    }

    private void setField(String name, Object value) throws Exception {
        Field field = JwtService.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(service, value);
    }
}
