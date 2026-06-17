package com.portfolio.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;

@DisplayName("GlobalExceptionHandler - Tratamento de exceções")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleNotFound() deve retornar 404")
    void handleNotFound_shouldReturn404() {
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(
                new ResourceNotFoundException("Recurso não encontrado")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso não encontrado", response.getBody().get("message"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
    @DisplayName("handleBusiness() deve retornar 422")
    void handleBusiness_shouldReturn422() {
        ResponseEntity<Map<String, Object>> response = handler.handleBusiness(
                new BusinessException("Regra de negócio violada")
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Regra de negócio violada", response.getBody().get("message"));
        assertEquals(422, response.getBody().get("status"));
    }

    @Test
    @DisplayName("handleValidation() deve retornar 400 com erros de validação")
    void handleValidation_shouldReturn400() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new FieldError("obj", "name", "Nome é obrigatório"));
        bindingResult.addError(new FieldError("obj", "email", "Email inválido"));

        Method method = this.getClass().getDeclaredMethod("handleValidation_shouldReturn400");
        MethodParameter parameter = new MethodParameter(method, -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String message = (String) response.getBody().get("message");
        assertNotNull(message);
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    @DisplayName("handleGeneric() deve retornar 500")
    void handleGeneric_shouldReturn500() {
        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(
                new RuntimeException("Erro inesperado")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno: Erro inesperado", response.getBody().get("message"));
        assertEquals(500, response.getBody().get("status"));
    }
}
