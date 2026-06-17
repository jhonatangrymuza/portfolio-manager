package com.portfolio.dto;

import com.portfolio.enums.ProjectStatus;

import java.time.LocalDate;

public record ProjectFilterRequest(
        String name,
        ProjectStatus status,
        Long managerId,
        LocalDate startDateFrom,
        LocalDate startDateTo
) {}
