package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
