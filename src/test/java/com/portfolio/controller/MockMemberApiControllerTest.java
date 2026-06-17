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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;
import com.portfolio.service.MemberService;

@WebMvcTest(MockMemberApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("MockMemberApiController - Mock de API externa")
class MockMemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("POST /api/mock/members deve criar membro")
    void create_shouldReturnCreated() throws Exception {
        MemberRequest request = new MemberRequest("João", "funcionário");
        MemberResponse response = new MemberResponse(1L, "João", "funcionário");

        when(memberService.create(any(MemberRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/mock/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/mock/members deve retornar lista")
    void findAll_shouldReturnList() throws Exception {
        when(memberService.findAll()).thenReturn(List.of(
                new MemberResponse(1L, "João", "funcionário")
        ));

        mockMvc.perform(get("/api/mock/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/mock/members/{id} deve retornar membro")
    void findById_shouldReturnMember() throws Exception {
        when(memberService.findById(1L)).thenReturn(new MemberResponse(1L, "João", "funcionário"));

        mockMvc.perform(get("/api/mock/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
