package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}
