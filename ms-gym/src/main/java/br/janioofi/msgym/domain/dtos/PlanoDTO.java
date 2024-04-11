package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlanoDTO {
    private Long id_plano;
    private String descricao;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate vigencia;
    private BigDecimal preco;
}
