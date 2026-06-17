package com.portfolio.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.dto.PortfolioReportResponse;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Project;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final ProjectService projectService;

    public ReportService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public PortfolioReportResponse generatePortfolioReport() {
        List<Project> allProjects = projectService.findAllWithDetails();

        Map<ProjectStatus, Long> countByStatus = allProjects.stream()
                .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));

        Map<ProjectStatus, BigDecimal> budgetByStatus = allProjects.stream()
                .collect(Collectors.groupingBy(
                        Project::getStatus,
                        Collectors.reducing(BigDecimal.ZERO, Project::getTotalBudget, BigDecimal::add)
                ));

        Double avgDuration = allProjects.stream()
                .filter(p -> p.getStatus() == ProjectStatus.ENCERRADO
                        && p.getActualEndDate() != null
                        && p.getStartDate() != null)
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getStartDate(), p.getActualEndDate()))
                .average()
                .orElse(0.0);

        long uniqueMembers = projectService.countDistinctAllocatedMembers();

        return new PortfolioReportResponse(countByStatus, budgetByStatus, avgDuration, uniqueMembers);
    }
}
