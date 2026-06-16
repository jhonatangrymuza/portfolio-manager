package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Atribuição é obrigatória") String role
) {}
