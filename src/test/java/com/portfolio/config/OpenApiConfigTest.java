package com.portfolio.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;

@DisplayName("OpenApiConfig - Configuração do Swagger")
class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    @DisplayName("openAPI() deve retornar configuração com título correto")
    void openAPI_shouldReturnConfiguredOpenAPI() {
        OpenAPI openAPI = config.openAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Portfolio Manager API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getComponents());
    }
}
