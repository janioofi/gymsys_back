package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id_usuario;
    private String usuario;
    private String senha;
    private Perfil perfil;
}
