package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ClienteDTO (
    Long id_cliente,
    String nome,
    String sobrenome,
    String apelido,
    String cpf,
    String email,
    @JsonFormat(pattern = "dd/MM/yyyy") @Past LocalDate data_nascimento,
    Long plano
){}
