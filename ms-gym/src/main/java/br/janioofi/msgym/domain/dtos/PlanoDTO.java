package br.janioofi.msgym.domain.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlanoDTO {
    private Long id_plano;
    private String descricao;
    private LocalDate vigencia;
    private BigDecimal preco;
}
