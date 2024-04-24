package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.Perfil;

import java.util.Set;

public record UsuarioRegistro(String usuario, String senha, Set<Perfil> perfis) {
}