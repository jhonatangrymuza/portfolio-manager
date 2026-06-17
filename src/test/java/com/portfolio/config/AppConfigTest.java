package com.portfolio.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

@DisplayName("AppConfig - Configuração de beans")
class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    @DisplayName("restTemplate() deve retornar instância não nula")
    void restTemplate_shouldReturnNonNullInstance() {
        RestTemplate restTemplate = appConfig.restTemplate();
        assertNotNull(restTemplate);
    }
}
