package com.portfolio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.dto.ProjectRequest;
import com.portfolio.dto.ProjectResponse;
import com.portfolio.dto.StatusUpdateRequest;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.enums.RiskLevel;
import com.portfolio.service.ProjectService;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ProjectController - Endpoints de projetos")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    private ProjectResponse buildResponse(Long id, String name) {
        return new ProjectResponse(
                id, name, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1),
                null, new BigDecimal("50000"), "Desc",
                new com.portfolio.dto.MemberResponse(1L, "Manager", "gerente"),
                ProjectStatus.EM_ANALISE, RiskLevel.BAIXO, List.of()
        );
    }

    @Test
    @DisplayName("POST /api/projects deve criar projeto")
    void create_shouldReturnCreated() throws Exception {
        ProjectRequest request = new ProjectRequest(
                "Novo Projeto", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1),
                null, new BigDecimal("50000"), "Desc", 1L
        );
        ProjectResponse response = buildResponse(1L, "Novo Projeto");

        when(projectService.create(any(ProjectRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Novo Projeto"));
    }

    @Test
    @DisplayName("GET /api/projects deve retornar página de projetos")
    void findAll_shouldReturnPage() throws Exception {
        Page<ProjectResponse> page = new PageImpl<>(List.of(buildResponse(1L, "Projeto 1")));
        when(projectService.findAll(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/projects/{id} deve retornar projeto")
    void findById_shouldReturnProject() throws Exception {
        when(projectService.findById(1L)).thenReturn(buildResponse(1L, "Projeto 1"));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/projects/{id} deve atualizar projeto")
    void update_shouldReturnOk() throws Exception {
        ProjectRequest request = new ProjectRequest(
                "Atualizado", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1),
                null, new BigDecimal("50000"), "Desc", 1L
        );
        when(projectService.update(eq(1L), any(ProjectRequest.class)))
                .thenReturn(buildResponse(1L, "Atualizado"));

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizado"));
    }

    @Test
    @DisplayName("DELETE /api/projects/{id} deve excluir projeto")
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(projectService).delete(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/projects/{id}/status deve atualizar status")
    void updateStatus_shouldReturnOk() throws Exception {
        StatusUpdateRequest request = new StatusUpdateRequest(ProjectStatus.ANALISE_REALIZADA);
        when(projectService.updateStatus(1L, ProjectStatus.ANALISE_REALIZADA))
                .thenReturn(buildResponse(1L, "Projeto 1"));

        mockMvc.perform(patch("/api/projects/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/projects/{projectId}/members/{memberId} deve adicionar membro")
    void addMember_shouldReturnOk() throws Exception {
        when(projectService.addMember(1L, 2L)).thenReturn(buildResponse(1L, "Projeto 1"));

        mockMvc.perform(post("/api/projects/1/members/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/projects/{projectId}/members/{memberId} deve remover membro")
    void removeMember_shouldReturnOk() throws Exception {
        when(projectService.removeMember(1L, 2L)).thenReturn(buildResponse(1L, "Projeto 1"));

        mockMvc.perform(delete("/api/projects/1/members/2"))
                .andExpect(status().isOk());
    }
}
