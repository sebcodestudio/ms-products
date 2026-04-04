package com.sebcode.msproducts.security;

import com.sebcode.msproducts.security.config.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtTokenProvider")
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    private static final String SECRET =
            "test-secret-key-for-unit-tests-that-is-long-enough-64-chars-minimum";

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationInMs", 86400000);
    }

    @Test
    @DisplayName("debe generar un token válido")
    void shouldGenerateValidToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin@sebcode.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = tokenProvider.generateToken(auth);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("debe extraer el username del token correctamente")
    void shouldExtractUsername() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin@sebcode.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = tokenProvider.generateToken(auth);
        String username = tokenProvider.getUsernameFromToken(token);

        assertThat(username).isEqualTo("admin@sebcode.com");
    }

    @Test
    @DisplayName("debe generar token desde username directamente")
    void shouldGenerateTokenFromUsername() {
        String token = tokenProvider.generateTokenFromUsername("admin@sebcode.com", "ROLE_ADMIN");

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateToken(token)).isTrue();
        assertThat(tokenProvider.getUsernameFromToken(token)).isEqualTo("admin@sebcode.com");
    }

    @Test
    @DisplayName("debe rechazar un token inválido")
    void shouldRejectInvalidToken() {
        assertThat(tokenProvider.validateToken("token.invalido.fake")).isFalse();
    }

    @Test
    @DisplayName("debe rechazar un token vacío")
    void shouldRejectBlankToken() {
        assertThat(tokenProvider.validateToken("")).isFalse();
    }
}