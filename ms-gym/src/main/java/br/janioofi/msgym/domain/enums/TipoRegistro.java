package br.janioofi.msgym.domain.enums;

public enum TipoRegistro {
    ENTRADA(0, "ENTRADA"),
    SAIDA(1, "SAIDA");

    private final Integer codigo;
    private final String descricao;

    TipoRegistro(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
