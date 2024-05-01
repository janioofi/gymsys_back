package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record PagamentoDTO (
    Long id_pagamento,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") FormaPagamento forma_pagamento,
    String cliente,
    Long id_cliente,
    BigDecimal valor
){}
