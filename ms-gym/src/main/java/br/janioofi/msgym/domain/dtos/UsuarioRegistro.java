package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.Perfil;

public record UsuarioRegistro(String usuario, String senha, Perfil perfil) {
}