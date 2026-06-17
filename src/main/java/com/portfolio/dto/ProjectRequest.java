package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotNull(message = "Data de início é obrigatória") LocalDate startDate,
        @NotNull(message = "Previsão de término é obrigatória") LocalDate estimatedEndDate,
        LocalDate actualEndDate,
        @NotNull(message = "Orçamento total é obrigatório")
        @Positive(message = "Orçamento deve ser positivo") BigDecimal totalBudget,
        String description,
        @NotNull(message = "ID do gerente é obrigatório") Long managerId
) {}
