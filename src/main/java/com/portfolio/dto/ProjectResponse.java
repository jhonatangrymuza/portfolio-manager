package com.portfolio.dto;

import com.portfolio.enums.ProjectStatus;
import com.portfolio.enums.RiskLevel;
import com.portfolio.model.Project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate estimatedEndDate,
        LocalDate actualEndDate,
        BigDecimal totalBudget,
        String description,
        MemberResponse manager,
        ProjectStatus status,
        RiskLevel riskLevel,
        List<MemberResponse> members
) {
    public static ProjectResponse from(Project project) {
        List<MemberResponse> members = project.getProjectMembers().stream()
                .map(pm -> MemberResponse.from(pm.getMember()))
                .toList();

        RiskLevel risk = RiskLevel.calculate(
                project.getTotalBudget(),
                project.getStartDate(),
                project.getEstimatedEndDate()
        );

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getStartDate(),
                project.getEstimatedEndDate(),
                project.getActualEndDate(),
                project.getTotalBudget(),
                project.getDescription(),
                MemberResponse.from(project.getManager()),
                project.getStatus(),
                risk,
                members
        );
    }
}
