package br.janioofi.msgym.domain.DTOS;

import br.janioofi.msgym.domain.enums.Perfil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Long id_usuario;
    private String usuario;
    private String senha;
    private Perfil perfil;
}
