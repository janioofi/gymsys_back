package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record PagamentoDTO (
    Long id_pagamento,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") FormaPagamento forma_pagamento,
    Long cliente,
    BigDecimal valor
){}
