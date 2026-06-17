package com.portfolio.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.enums.ProjectStatus;
import com.portfolio.enums.RiskLevel;
import com.portfolio.model.Member;
import com.portfolio.model.Project;

@DisplayName("ProjectResponse - Conversão de entidade")
class ProjectResponseTest {

    @Test
    @DisplayName("from() deve converter Project para ProjectResponse")
    void from_shouldConvertProjectToResponse() {
        Member manager = new Member();
        manager.setId(1L);
        manager.setName("Manager");
        manager.setRole("gerente");

        Project project = new Project();
        project.setId(1L);
        project.setName("Projeto Teste");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEstimatedEndDate(LocalDate.of(2024, 3, 1));
        project.setActualEndDate(null);
        project.setTotalBudget(new BigDecimal("50000"));
        project.setDescription("Descrição");
        project.setManager(manager);
        project.setStatus(ProjectStatus.EM_ANALISE);
        project.setProjectMembers(new ArrayList<>());

        ProjectResponse response = ProjectResponse.from(project);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Projeto Teste", response.name());
        assertEquals(ProjectStatus.EM_ANALISE, response.status());
        assertEquals(RiskLevel.BAIXO, response.riskLevel());
        assertNotNull(response.manager());
        assertEquals("Manager", response.manager().name());
    }
}
