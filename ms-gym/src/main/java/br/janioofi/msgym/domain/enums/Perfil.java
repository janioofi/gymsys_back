package br.janioofi.msgym.domain.enums;

import lombok.Getter;

@Getter
public enum Perfil {
    ADMIN(0, "ROLE_ADMIN"),
    PROFISSIONAL(1, "ROLE_PROFISSIONAL");

    private final Integer codigo;
    private final String descricao;

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
