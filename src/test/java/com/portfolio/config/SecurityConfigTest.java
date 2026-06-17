package com.portfolio.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("SecurityConfig - Configuração de segurança")
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    @DisplayName("passwordEncoder() deve retornar PasswordEncoder não nulo")
    void passwordEncoder_shouldReturnNonNull() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    @DisplayName("userDetailsService() deve retornar UserDetailsService não nulo")
    void userDetailsService_shouldReturnNonNull() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        UserDetailsService service = securityConfig.userDetailsService(encoder);
        assertNotNull(service);
    }
}
