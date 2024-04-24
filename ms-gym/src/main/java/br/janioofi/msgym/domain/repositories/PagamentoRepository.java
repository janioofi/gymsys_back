package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query(value =
            " WITH ultimo_pagamento as (" +
            " SELECT id_cliente, MAX(pag.data_pagamento) AS data_pagamento" +
            " FROM pagamento pag " +
            " WHERE pag.id_cliente = :id_cliente " +
            " GROUP BY id_cliente " +
            ") " +
            "SELECT pag.* FROM pagamento pag " +
            "JOIN ultimo_pagamento up ON up.id_cliente = pag.id_cliente AND pag.data_pagamento = up.data_pagamento", nativeQuery = true)
    Pagamento findByUltimoPagamento(@Param("id_cliente") Long id_cliente);
}
