package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query(value = """
    WITH
    ultimo_pagamento as (
    SELECT id_cliente, MAX(pag.data_pagamento) AS data_pagamento
    FROM pagamento pag
    WHERE pag.id_cliente = :id_cliente
    GROUP BY id_cliente )
    SELECT pag.* FROM pagamento pag
    JOIN ultimo_pagamento up ON up.id_cliente = pag.id_cliente AND pag.data_pagamento = up.data_pagamento
    """, nativeQuery = true)
    Optional<Pagamento> findByUltimoPagamento(@Param("id_cliente") Long id_cliente);

    @Query(value = """
    SELECT * FROM pagamento p
    WHERE CAST(p.data_pagamento AS date) BETWEEN CAST(:data_inicio AS date) AND CAST(:data_final AS date)
    ORDER BY p.data_pagamento;
    """, nativeQuery = true)
    List<Pagamento> pagamentosPorPeriodo(@Param("data_inicio") String data_inicio, @Param("data_final") String data_final);

    @Query(value = "SELECT * FROM pagamento WHERE id_cliente = :id_cliente", nativeQuery = true)
    Optional<List<Pagamento>> pagamentosPorCliente(@Param("id_cliente") Long id_cliente);
}
