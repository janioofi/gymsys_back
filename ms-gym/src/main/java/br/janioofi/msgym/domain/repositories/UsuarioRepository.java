package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
