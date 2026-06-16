package com.portfolio.enums;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum RiskLevel {
    BAIXO,
    MEDIO,
    ALTO;

    public static RiskLevel calculate(BigDecimal budget, LocalDate startDate, LocalDate estimatedEndDate) {
        if (budget == null || startDate == null || estimatedEndDate == null) {
            return ALTO;
        }

        long months = ChronoUnit.MONTHS.between(startDate, estimatedEndDate);
        BigDecimal lowThreshold = new BigDecimal("100000");
        BigDecimal highThreshold = new BigDecimal("500000");

        boolean highBudget = budget.compareTo(highThreshold) > 0;
        boolean highDuration = months > 6;
        boolean mediumBudget = budget.compareTo(lowThreshold) > 0 && budget.compareTo(highThreshold) <= 0;
        boolean mediumDuration = months > 3 && months <= 6;
        boolean lowBudget = budget.compareTo(lowThreshold) <= 0;
        boolean lowDuration = months <= 3;

        if (highBudget || highDuration) {
            return ALTO;
        }
        if (mediumBudget || mediumDuration) {
            return MEDIO;
        }
        if (lowBudget && lowDuration) {
            return BAIXO;
        }
        return MEDIO;
    }
}
