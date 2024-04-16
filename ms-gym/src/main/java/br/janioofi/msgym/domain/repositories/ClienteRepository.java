package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmail(String email);
}
