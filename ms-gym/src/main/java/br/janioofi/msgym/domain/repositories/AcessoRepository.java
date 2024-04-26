package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {
}
