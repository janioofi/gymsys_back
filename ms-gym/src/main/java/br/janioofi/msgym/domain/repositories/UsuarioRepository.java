package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
}
