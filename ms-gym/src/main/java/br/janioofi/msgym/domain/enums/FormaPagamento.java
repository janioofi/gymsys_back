package br.janioofi.msgym.domain.enums;

public enum FormaPagamento {
    PIX(0, "PIX"),
    CREDITO(1, "CRÉDITO"),
    DEBITO(2, "DÉBITO"),
    DINHEIRO(3, "DINHEIRO");

    private final Integer codigo;
    private final String descricao;

    FormaPagamento(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}

