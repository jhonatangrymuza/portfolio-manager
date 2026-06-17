package com.portfolio.enums;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("ProjectStatus - Regras de transição de status")
class ProjectStatusTest {

    @Test
    @DisplayName("Deve permitir transições sequenciais válidas")
    void shouldAllowAllSequentialTransitions() {
        assertTrue(ProjectStatus.EM_ANALISE.canTransitionTo(ProjectStatus.ANALISE_REALIZADA));
        assertTrue(ProjectStatus.ANALISE_REALIZADA.canTransitionTo(ProjectStatus.ANALISE_APROVADA));
        assertTrue(ProjectStatus.ANALISE_APROVADA.canTransitionTo(ProjectStatus.INICIADO));
        assertTrue(ProjectStatus.INICIADO.canTransitionTo(ProjectStatus.PLANEJADO));
        assertTrue(ProjectStatus.PLANEJADO.canTransitionTo(ProjectStatus.EM_ANDAMENTO));
        assertTrue(ProjectStatus.EM_ANDAMENTO.canTransitionTo(ProjectStatus.ENCERRADO));
    }

    @Test
    @DisplayName("Não deve permitir pular etapas")
    void shouldRejectSkippingSteps() {
        assertFalse(ProjectStatus.EM_ANALISE.canTransitionTo(ProjectStatus.ANALISE_APROVADA));
        assertFalse(ProjectStatus.EM_ANALISE.canTransitionTo(ProjectStatus.INICIADO));
        assertFalse(ProjectStatus.EM_ANALISE.canTransitionTo(ProjectStatus.ENCERRADO));
        assertFalse(ProjectStatus.ANALISE_REALIZADA.canTransitionTo(ProjectStatus.INICIADO));
        assertFalse(ProjectStatus.INICIADO.canTransitionTo(ProjectStatus.EM_ANDAMENTO));
        assertFalse(ProjectStatus.PLANEJADO.canTransitionTo(ProjectStatus.ENCERRADO));
    }

    @Test
    @DisplayName("Não deve permitir retroceder no fluxo")
    void shouldRejectBackwardTransitions() {
        assertFalse(ProjectStatus.ANALISE_REALIZADA.canTransitionTo(ProjectStatus.EM_ANALISE));
        assertFalse(ProjectStatus.INICIADO.canTransitionTo(ProjectStatus.ANALISE_APROVADA));
        assertFalse(ProjectStatus.EM_ANDAMENTO.canTransitionTo(ProjectStatus.PLANEJADO));
    }

    @ParameterizedTest
    @DisplayName("CANCELADO deve ser permitido de qualquer status não terminal")
    @EnumSource(value = ProjectStatus.class,
            names = {"EM_ANALISE", "ANALISE_REALIZADA", "ANALISE_APROVADA", "INICIADO", "PLANEJADO", "EM_ANDAMENTO"})
    void shouldAllowCancelledFromAnyNonTerminalStatus(ProjectStatus status) {
        assertTrue(status.canTransitionTo(ProjectStatus.CANCELADO));
    }

    @Test
    @DisplayName("Não deve permitir CANCELADO a partir de ENCERRADO")
    void shouldRejectCancelledFromEncerrado() {
        assertFalse(ProjectStatus.ENCERRADO.canTransitionTo(ProjectStatus.CANCELADO));
    }

    @Test
    @DisplayName("Não deve permitir nenhuma transição a partir de CANCELADO")
    void shouldRejectAnyTransitionFromCancelado() {
        for (ProjectStatus next : ProjectStatus.values()) {
            assertFalse(ProjectStatus.CANCELADO.canTransitionTo(next),
                    "CANCELADO não deveria transicionar para " + next);
        }
    }

    @Test
    @DisplayName("Não deve permitir nenhuma transição a partir de ENCERRADO")
    void shouldRejectAnyTransitionFromEncerrado() {
        for (ProjectStatus next : ProjectStatus.values()) {
            assertFalse(ProjectStatus.ENCERRADO.canTransitionTo(next),
                    "ENCERRADO não deveria transicionar para " + next);
        }
    }

    @Test
    @DisplayName("isDeletable() deve permitir exclusão para status não bloqueados")
    void isDeletable_shouldAllowForNonBlockedStatuses() {
        assertTrue(ProjectStatus.EM_ANALISE.isDeletable());
        assertTrue(ProjectStatus.ANALISE_REALIZADA.isDeletable());
        assertTrue(ProjectStatus.ANALISE_APROVADA.isDeletable());
        assertTrue(ProjectStatus.PLANEJADO.isDeletable());
        assertTrue(ProjectStatus.CANCELADO.isDeletable());
    }

    @Test
    @DisplayName("isDeletable() deve bloquear exclusão para INICIADO, EM_ANDAMENTO e ENCERRADO")
    void isDeletable_shouldBlockForRestrictedStatuses() {
        assertFalse(ProjectStatus.INICIADO.isDeletable());
        assertFalse(ProjectStatus.EM_ANDAMENTO.isDeletable());
        assertFalse(ProjectStatus.ENCERRADO.isDeletable());
    }
}
