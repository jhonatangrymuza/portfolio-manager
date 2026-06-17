package com.portfolio.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberApiClient - Comunicação com API externa")
class MemberApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    private MemberApiClient client;

    @BeforeEach
    void setUp() {
        client = new MemberApiClient(restTemplate, "http://localhost:8080");
    }

    @Test
    @DisplayName("create() deve enviar POST e retornar MemberResponse")
    void create_shouldPostAndReturnResponse() {
        MemberRequest request = new MemberRequest("João", "funcionário");
        MemberResponse expected = new MemberResponse(1L, "João", "funcionário");

        when(restTemplate.postForObject(
                eq("http://localhost:8080/api/mock/members"),
                eq(request),
                eq(MemberResponse.class)
        )).thenReturn(expected);

        MemberResponse result = client.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("João", result.name());
    }

    @Test
    @DisplayName("findAll() deve enviar GET e retornar lista de membros")
    void findAll_shouldGetAndReturnList() {
        List<MemberResponse> expected = List.of(
                new MemberResponse(1L, "João", "funcionário"),
                new MemberResponse(2L, "Maria", "gerente")
        );
        ResponseEntity<List<MemberResponse>> response = ResponseEntity.ok(expected);
        ParameterizedTypeReference<List<MemberResponse>> typeRef = new ParameterizedTypeReference<>() {};

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/mock/members"),
                eq(HttpMethod.GET),
                eq(null),
                eq(typeRef)
        )).thenReturn(response);

        List<MemberResponse> result = client.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findById() deve enviar GET e retornar membro")
    void findById_shouldGetAndReturnMember() {
        MemberResponse expected = new MemberResponse(1L, "João", "funcionário");

        when(restTemplate.getForObject(
                eq("http://localhost:8080/api/mock/members/1"),
                eq(MemberResponse.class)
        )).thenReturn(expected);

        MemberResponse result = client.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }
}
