package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteDTO (
        Long id_cliente,
        String nome,
        String sobrenome,
        String apelido,
        String cpf,
        String email,
        @JsonFormat(pattern = "yyyy-MM-dd") @Past LocalDate data_nascimento,
        String plano,
        Long id_plano
){}
