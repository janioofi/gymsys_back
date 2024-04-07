package br.janioofi.msgym.domain.entities;

import br.janioofi.msgym.domain.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotNull
    @Column(unique = true)
    private String usuario;

    @NotNull
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    public Usuarios(Long id_usuario, String usuario, String senha, Perfil perfil) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.senha = senha;
        this.perfil = perfil;
    }

    public Usuarios() {
    }
}
