package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDTO {
    private Long id_pagamento;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private FormaPagamento forma_pagamento;
    private Long cliente;
    private Long plano;
    private BigDecimal valor;
}
