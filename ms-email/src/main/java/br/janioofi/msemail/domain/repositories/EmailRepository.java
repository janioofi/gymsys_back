package br.janioofi.msemail.domain.repositories;

import br.janioofi.msemail.domain.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
