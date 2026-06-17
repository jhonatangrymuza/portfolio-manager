package com.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portfolio.dto.PortfolioReportResponse;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Member;
import com.portfolio.model.Project;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService - Geração de relatórios")
class ReportServiceTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ReportService reportService;

    private Member manager;

    @BeforeEach
    void setUp() {
        manager = new Member();
        manager.setId(1L);
        manager.setName("Manager");
        manager.setRole("gerente");
    }

    private Project buildProject(Long id, ProjectStatus status, BigDecimal budget,
                                  LocalDate startDate, LocalDate actualEndDate) {
        Project project = new Project();
        project.setId(id);
        project.setName("Projeto " + id);
        project.setStatus(status);
        project.setTotalBudget(budget);
        project.setStartDate(startDate);
        project.setEstimatedEndDate(startDate.plusMonths(3));
        project.setActualEndDate(actualEndDate);
        project.setManager(manager);
        return project;
    }

    @Test
    @DisplayName("generatePortfolioReport() deve retornar relatório com métricas corretas")
    void generatePortfolioReport_shouldReturnCorrectMetrics() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        Project p1 = buildProject(1L, ProjectStatus.EM_ANALISE, new BigDecimal("50000"), start, null);
        Project p2 = buildProject(2L, ProjectStatus.ENCERRADO, new BigDecimal("100000"), start, start.plusDays(30));
        Project p3 = buildProject(3L, ProjectStatus.ENCERRADO, new BigDecimal("200000"), start, start.plusDays(60));

        when(projectService.findAllWithDetails()).thenReturn(List.of(p1, p2, p3));
        when(projectService.countDistinctAllocatedMembers()).thenReturn(5L);

        PortfolioReportResponse report = reportService.generatePortfolioReport();

        assertNotNull(report);
        Map<ProjectStatus, Long> countByStatus = report.projectCountByStatus();
        assertEquals(1L, countByStatus.get(ProjectStatus.EM_ANALISE));
        assertEquals(2L, countByStatus.get(ProjectStatus.ENCERRADO));

        Map<ProjectStatus, BigDecimal> budgetByStatus = report.totalBudgetByStatus();
        assertEquals(new BigDecimal("50000"), budgetByStatus.get(ProjectStatus.EM_ANALISE));
        assertEquals(new BigDecimal("300000"), budgetByStatus.get(ProjectStatus.ENCERRADO));

        assertEquals(45.0, report.averageDurationOfClosedProjects());
        assertEquals(5L, report.totalUniqueAllocatedMembers());
    }

    @Test
    @DisplayName("generatePortfolioReport() deve retornar 0.0 quando não há projetos encerrados")
    void generatePortfolioReport_shouldReturnZeroAverage_whenNoClosedProjects() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        Project p1 = buildProject(1L, ProjectStatus.EM_ANALISE, new BigDecimal("50000"), start, null);

        when(projectService.findAllWithDetails()).thenReturn(List.of(p1));
        when(projectService.countDistinctAllocatedMembers()).thenReturn(0L);

        PortfolioReportResponse report = reportService.generatePortfolioReport();

        assertEquals(0.0, report.averageDurationOfClosedProjects());
    }

    @Test
    @DisplayName("generatePortfolioReport() deve ignorar projetos encerrados sem data real de término")
    void generatePortfolioReport_shouldIgnoreClosedProjectsWithoutActualEndDate() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        Project p1 = buildProject(1L, ProjectStatus.ENCERRADO, new BigDecimal("100000"), start, null);
        Project p2 = buildProject(2L, ProjectStatus.ENCERRADO, new BigDecimal("200000"), start, start.plusDays(60));

        when(projectService.findAllWithDetails()).thenReturn(List.of(p1, p2));
        when(projectService.countDistinctAllocatedMembers()).thenReturn(2L);

        PortfolioReportResponse report = reportService.generatePortfolioReport();

        assertEquals(60.0, report.averageDurationOfClosedProjects());
    }
}
