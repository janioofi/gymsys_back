package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


public record PlanoDTO (
    Long id_plano,
    String descricao,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate vigencia,
    BigDecimal preco,
    Long quantidadeMeses
){}
