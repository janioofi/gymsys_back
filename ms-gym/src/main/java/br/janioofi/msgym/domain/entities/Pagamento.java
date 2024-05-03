package br.janioofi.msgym.domain.entities;

import br.janioofi.msgym.domain.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pagamento;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime data_pagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FormaPagamento forma_pagamento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Enumerated(EnumType.ORDINAL)
    private Cliente cliente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_plano")
    @Enumerated(EnumType.ORDINAL)
    private Plano plano;

    @NotNull
    private BigDecimal valor;

    private String observacao;
}

