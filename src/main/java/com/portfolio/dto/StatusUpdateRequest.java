package com.portfolio.dto;

import com.portfolio.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Status é obrigatório") ProjectStatus status
) {}
