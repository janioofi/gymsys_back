package br.janioofi.msgym.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Plano implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_plano;

    @NotNull(message = "Descrição é obrigatória")
    @NotEmpty(message = "Descrição é obrigatória")
    @Column(unique = true)
    private String descricao;

    @NotNull(message = "Vigência é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate vigencia;

    @NotNull(message = "Preço é obrigatória")
    private BigDecimal preco;
}
