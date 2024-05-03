package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoDTO (
    Long id_pagamento,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime data_pagamento,
    FormaPagamento forma_pagamento,
    String plano,
    String cliente,
    Long id_cliente,
    BigDecimal valor,
    String observacao
){}
