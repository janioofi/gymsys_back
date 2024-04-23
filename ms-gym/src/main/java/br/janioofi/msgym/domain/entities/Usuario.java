package br.janioofi.msgym.domain.entities;

import br.janioofi.msgym.domain.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotNull(message = "Usuário é obrigatório")
    @NotEmpty(message = "Usuário não pode estar vazio")
    @Column(unique = true)
    private String usuario;

    @NotNull(message = "Senha é obrigatória")
    @NotEmpty(message = "Senha não pode estar vazio")
    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(perfil.getDescricao()));
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}