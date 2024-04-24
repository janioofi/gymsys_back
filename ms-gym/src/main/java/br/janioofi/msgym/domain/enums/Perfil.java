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

    public static Perfil toEnum(Integer cod){
        if(cod == null)return null;
        for(Perfil x : Perfil.values()){
            if(cod.equals(x.getCodigo())){
                return x;
            }
        }
        throw new IllegalArgumentException("Perfil inválido");
    }
}
