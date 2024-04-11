package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
    Optional<Plano> findByDescricao(String descricao);
}
