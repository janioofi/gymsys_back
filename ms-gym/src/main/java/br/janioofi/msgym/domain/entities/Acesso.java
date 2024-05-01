package br.janioofi.msgym.domain.entities;

import br.janioofi.msgym.domain.enums.TipoRegistro;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_acesso;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Enumerated(EnumType.ORDINAL)
    private Cliente cliente;
    private String cpf;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime data_registro;
    @Enumerated(EnumType.STRING)
    private TipoRegistro tipoRegistro;
}
