package com.portfolio.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("RiskLevel - Cálculo dinâmico de risco")
class RiskLevelTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);

    @Test
    @DisplayName("BAIXO: orçamento <= 100k e prazo <= 3 meses")
    void shouldReturnBaixo_whenBudgetAndDurationAreLow() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("100000"),
                BASE_DATE,
                BASE_DATE.plusMonths(3)
        );
        assertEquals(RiskLevel.BAIXO, result);
    }

    @Test
    @DisplayName("BAIXO: orçamento abaixo do limite e prazo curto")
    void shouldReturnBaixo_whenBudgetBelowThresholdAndShortDuration() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("50000"),
                BASE_DATE,
                BASE_DATE.plusWeeks(4)
        );
        assertEquals(RiskLevel.BAIXO, result);
    }

    @Test
    @DisplayName("MEDIO: orçamento entre 100k e 500k, prazo curto")
    void shouldReturnMedio_whenBudgetIsMedium() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("200000"),
                BASE_DATE,
                BASE_DATE.plusMonths(1)
        );
        assertEquals(RiskLevel.MEDIO, result);
    }

    @Test
    @DisplayName("MEDIO: orçamento exatamente 100001 (acima do limite baixo)")
    void shouldReturnMedio_whenBudgetExceedsLowThresholdByOne() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("100001"),
                BASE_DATE,
                BASE_DATE.plusMonths(1)
        );
        assertEquals(RiskLevel.MEDIO, result);
    }

    @Test
    @DisplayName("MEDIO: prazo entre 3 e 6 meses, orçamento baixo")
    void shouldReturnMedio_whenDurationIsMedium() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("50000"),
                BASE_DATE,
                BASE_DATE.plusMonths(4)
        );
        assertEquals(RiskLevel.MEDIO, result);
    }

    @Test
    @DisplayName("ALTO: orçamento acima de 500k")
    void shouldReturnAlto_whenBudgetIsHigh() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("600000"),
                BASE_DATE,
                BASE_DATE.plusMonths(1)
        );
        assertEquals(RiskLevel.ALTO, result);
    }

    @Test
    @DisplayName("ALTO: orçamento exatamente 500001 (acima do limite médio)")
    void shouldReturnAlto_whenBudgetExceedsHighThresholdByOne() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("500001"),
                BASE_DATE,
                BASE_DATE.plusMonths(1)
        );
        assertEquals(RiskLevel.ALTO, result);
    }

    @Test
    @DisplayName("ALTO: prazo acima de 6 meses")
    void shouldReturnAlto_whenDurationExceedsSixMonths() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("50000"),
                BASE_DATE,
                BASE_DATE.plusMonths(7)
        );
        assertEquals(RiskLevel.ALTO, result);
    }

    @Test
    @DisplayName("ALTO: ambos orçamento e prazo altos")
    void shouldReturnAlto_whenBothBudgetAndDurationAreHigh() {
        RiskLevel result = RiskLevel.calculate(
                new BigDecimal("600000"),
                BASE_DATE,
                BASE_DATE.plusMonths(8)
        );
        assertEquals(RiskLevel.ALTO, result);
    }

    @Test
    @DisplayName("ALTO: parâmetros nulos devem retornar ALTO")
    void shouldReturnAlto_whenParametersAreNull() {
        assertEquals(RiskLevel.ALTO, RiskLevel.calculate(null, null, null));
    }

    @Test
    @DisplayName("ALTO: orçamento nulo retorna ALTO")
    void shouldReturnAlto_whenBudgetIsNull() {
        assertEquals(RiskLevel.ALTO, RiskLevel.calculate(null, BASE_DATE, BASE_DATE.plusMonths(1)));
    }
}
