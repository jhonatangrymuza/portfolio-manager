package com.portfolio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PortfolioManagerApplication - Inicialização")
class PortfolioManagerApplicationTest {

    @Test
    @DisplayName("main() deve iniciar sem exceções")
    void main_shouldStartWithoutExceptions() {
        assertDoesNotThrow(() -> PortfolioManagerApplication.main(new String[]{}));
    }
}
