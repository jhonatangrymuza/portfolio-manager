package com.portfolio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.client.MemberApiClient;
import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("MemberController - Endpoints de membros")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberApiClient memberApiClient;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("POST /api/members deve criar membro")
    void create_shouldReturnCreated() throws Exception {
        MemberRequest request = new MemberRequest("João", "funcionário");
        MemberResponse response = new MemberResponse(1L, "João", "funcionário");

        when(memberApiClient.create(any(MemberRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("GET /api/members deve retornar lista")
    void findAll_shouldReturnList() throws Exception {
        when(memberApiClient.findAll()).thenReturn(List.of(
                new MemberResponse(1L, "João", "funcionário")
        ));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("GET /api/members/{id} deve retornar membro")
    void findById_shouldReturnMember() throws Exception {
        when(memberApiClient.findById(1L)).thenReturn(new MemberResponse(1L, "João", "funcionário"));

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
