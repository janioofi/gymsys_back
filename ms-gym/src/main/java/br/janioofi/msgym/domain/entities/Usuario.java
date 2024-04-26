package br.janioofi.msgym.domain.entities;

import br.janioofi.msgym.domain.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.HashSet;
import java.util.Set;

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
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Perfil> perfis = new HashSet<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Perfil perfil: perfis) {
            authorities.add(new SimpleGrantedAuthority(perfil.getDescricao()));
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return getSenha();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getUsuario();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}