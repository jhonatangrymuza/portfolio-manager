package com.portfolio.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@DisplayName("ResourceNotFoundException - Exceção de recurso não encontrado")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("deve lançar com mensagem correta")
    void shouldStoreMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Não encontrado");
        assertEquals("Não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("deve ter anotação ResponseStatus NOT_FOUND")
    void shouldHaveCorrectStatus() {
        ResponseStatus annotation = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }
}
