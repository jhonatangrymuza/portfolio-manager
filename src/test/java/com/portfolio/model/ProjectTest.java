package com.portfolio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.enums.ProjectStatus;

@DisplayName("Project - Entidade de projeto")
class ProjectTest {

    @Test
    @DisplayName("deve criar Project com valores padrão")
    void shouldCreateWithDefaults() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Projeto");
        project.setStartDate(LocalDate.now());
        project.setEstimatedEndDate(LocalDate.now().plusMonths(1));
        project.setTotalBudget(new BigDecimal("10000"));

        assertNotNull(project);
        assertEquals(ProjectStatus.EM_ANALISE, project.getStatus());
        assertNotNull(project.getProjectMembers());
        assertTrue(project.getProjectMembers().isEmpty());
    }

    @Test
    @DisplayName("deve suportar getters e setters")
    void shouldSupportGettersAndSetters() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Nome");
        project.setDescription("Desc");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEstimatedEndDate(LocalDate.of(2024, 6, 1));
        project.setActualEndDate(LocalDate.of(2024, 5, 1));
        project.setTotalBudget(new BigDecimal("100000"));
        project.setStatus(ProjectStatus.ENCERRADO);

        Member manager = new Member();
        manager.setId(10L);
        project.setManager(manager);
        project.setProjectMembers(new ArrayList<>());

        assertEquals(1L, project.getId());
        assertEquals("Nome", project.getName());
        assertEquals("Desc", project.getDescription());
        assertEquals(LocalDate.of(2024, 1, 1), project.getStartDate());
        assertEquals(LocalDate.of(2024, 6, 1), project.getEstimatedEndDate());
        assertEquals(LocalDate.of(2024, 5, 1), project.getActualEndDate());
        assertEquals(new BigDecimal("100000"), project.getTotalBudget());
        assertEquals(ProjectStatus.ENCERRADO, project.getStatus());
        assertEquals(10L, project.getManager().getId());
        assertNotNull(project.getProjectMembers());
    }

    @Test
    @DisplayName("equals e hashCode devem funcionar corretamente")
    void shouldSupportEqualsAndHashCode() {
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("A");
        p1.setStartDate(LocalDate.now());
        p1.setEstimatedEndDate(LocalDate.now());
        p1.setTotalBudget(BigDecimal.ONE);

        Project p2 = new Project();
        p2.setId(1L);
        p2.setName("A");
        p2.setStartDate(LocalDate.now());
        p2.setEstimatedEndDate(LocalDate.now());
        p2.setTotalBudget(BigDecimal.ONE);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("toString deve retornar string não vazia")
    void shouldSupportToString() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Teste");
        project.setStartDate(LocalDate.now());
        project.setEstimatedEndDate(LocalDate.now());
        project.setTotalBudget(BigDecimal.TEN);

        String str = project.toString();
        assertNotNull(str);
        assertTrue(str.contains("Teste"));
    }

    @Test
    @DisplayName("construtor com todos os argumentos deve funcionar")
    void shouldSupportAllArgsConstructor() {
        Member manager = new Member();
        Project project = new Project(
                1L, "Nome", LocalDate.now(), LocalDate.now().plusDays(1),
                null, BigDecimal.TEN, "Desc", manager, ProjectStatus.EM_ANALISE, new ArrayList<>()
        );
        assertEquals("Nome", project.getName());
    }
}
