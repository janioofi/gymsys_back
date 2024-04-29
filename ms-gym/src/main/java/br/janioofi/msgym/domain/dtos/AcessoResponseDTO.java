package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.enums.TipoRegistro;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record AcessoResponseDTO(
        String cliente,
        String cpf,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime data_registro,
        @Enumerated(EnumType.STRING) TipoRegistro tipoRegistro
) {
}
