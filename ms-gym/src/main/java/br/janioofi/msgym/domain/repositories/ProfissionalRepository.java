package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    Optional<Profissional> findByCpf(String cpf);
    Optional<Profissional> findByEmail(String email);

    @Query(value = "SELECT * FROM profissional p WHERE p.id_usuario = :id", nativeQuery = true)
    Optional<Profissional> findByUsuario(Long  id);

}
