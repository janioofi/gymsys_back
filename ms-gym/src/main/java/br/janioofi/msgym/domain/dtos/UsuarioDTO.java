package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.Perfil;

import java.util.Set;

public record UsuarioDTO (
    Long id_usuario,
    String usuario,
    String senha,
    Set<Perfil> perfis
){}
