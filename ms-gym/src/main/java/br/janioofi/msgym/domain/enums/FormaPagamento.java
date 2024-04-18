package br.janioofi.msgym.domain.enums;

public enum FormaPagamento {
    PIX(0, "PIX"),
    CARTAO_CREDITO(1, "CARTÃO DE CRÉDITO"),
    CARTAO_DEBITO(2, "CARTÃO DE DÉBITO"),
    DINHEIRO(3, "DINHEIRO");

    private final Integer codigo;
    private final String descricao;

    FormaPagamento(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}

