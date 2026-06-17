package com.portfolio.dto;

import com.portfolio.enums.ProjectStatus;

import java.math.BigDecimal;
import java.util.Map;

public record PortfolioReportResponse(
        Map<ProjectStatus, Long> projectCountByStatus,
        Map<ProjectStatus, BigDecimal> totalBudgetByStatus,
        Double averageDurationOfClosedProjects,
        long totalUniqueAllocatedMembers
) {}
