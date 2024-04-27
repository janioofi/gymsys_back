package br.janioofi.msgym.domain.repositories;

import br.janioofi.msgym.domain.entities.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {
    @Query(value = """
    WITH
    ultimo_acesso AS (
    SELECT max(data_registro) AS data_registro, id_cliente FROM ACESSO
    WHERE id_cliente = :id_cliente
    AND tipo_registro = 'ENTRADA'
    GROUP BY id_cliente)
    SELECT a.* FROM acesso a
    JOIN ultimo_acesso uc ON uc.id_cliente = a.id_cliente AND uc.data_registro = a.data_registro;
    """, nativeQuery = true)
    Optional<Acesso> ultimaEntrada(@Param("id_cliente") Long id_cliente);

    @Query(value = """
    WITH
    ultimo_acesso AS (
    SELECT max(data_registro) AS data_registro, id_cliente FROM ACESSO
    WHERE id_cliente = :id_cliente
    GROUP BY id_cliente)
    SELECT a.* FROM acesso a
    JOIN ultimo_acesso uc ON uc.id_cliente = a.id_cliente AND uc.data_registro = a.data_registro;
    """, nativeQuery = true)
    Optional<Acesso> ultimoRegistro(@Param("id_cliente") Long id_cliente);

    @Query(value = "SELECT * FROM acesso a WHERE CAST(a.data_registro AS date) = CAST(:data_registro AS date) ORDER BY a.data_registro", nativeQuery = true)
    List<Acesso> findAllAcessosDoDia(@Param("data_registro") String data_registro);

    @Query(value = """
    SELECT * FROM acesso a
    WHERE CAST(a.data_registro AS date) BETWEEN CAST(:data_inicio AS date) AND CAST(:data_final AS date)
    ORDER BY a.data_registro;
    """, nativeQuery = true)
    List<Acesso> findAllAcessosPeriodo(@Param("data_inicio") String data_inicio, @Param("data_final") String data_final);

    @Query(value = """
    WITH
    ultimo_acesso AS (
    SELECT max(data_registro) AS data_registro, id_cliente FROM acesso
    WHERE CAST(data_registro AS date) = CAST(:data_registro AS date)
    GROUP BY id_cliente)
    SELECT a.* FROM ultimo_acesso uc
    JOIN acesso a ON a.id_cliente = uc.id_cliente AND uc.data_registro = a.data_registro
    WHERE a.tipo_registro = 'ENTRADA'
    AND CAST(a.data_registro AS date) = CAST(:data_registro AS date)
    ORDER BY a.data_registro
    """, nativeQuery = true)
    List<Acesso> treinandoAgora(@Param("data_registro") String data_registro);
}
