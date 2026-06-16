package com.portfolio.enums;

public enum ProjectStatus {
    EM_ANALISE(0),
    ANALISE_REALIZADA(1),
    ANALISE_APROVADA(2),
    INICIADO(3),
    PLANEJADO(4),
    EM_ANDAMENTO(5),
    ENCERRADO(6),
    CANCELADO(-1);

    private final int order;

    ProjectStatus(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public boolean isDeletable() {
        return this != INICIADO && this != EM_ANDAMENTO && this != ENCERRADO;
    }

    public boolean canTransitionTo(ProjectStatus next) {
        if (next == CANCELADO) {
            return this != ENCERRADO && this != CANCELADO;
        }
        if (this == CANCELADO || this == ENCERRADO) {
            return false;
        }
        return next.order == this.order + 1;
    }
}
