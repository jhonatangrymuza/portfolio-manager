package com.portfolio.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@DisplayName("BusinessException - Exceção de negócio")
class BusinessExceptionTest {

    @Test
    @DisplayName("deve lançar com mensagem correta")
    void shouldStoreMessage() {
        BusinessException ex = new BusinessException("Erro de negócio");
        assertEquals("Erro de negócio", ex.getMessage());
    }

    @Test
    @DisplayName("deve ter anotação ResponseStatus UNPROCESSABLE_ENTITY")
    void shouldHaveCorrectStatus() {
        ResponseStatus annotation = BusinessException.class.getAnnotation(ResponseStatus.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, annotation.value());
    }
}
