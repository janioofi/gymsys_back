package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ProfissionalDTO(
    Long id_profissional,
    String nome,
    String sobrenome,
    String cpf,
    String email,
    @JsonFormat(pattern = "yyyy-MM-dd") @Past LocalDate data_nascimento,
    @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd") LocalDate data_admissao,
    String usuario,
    Long id_usuario
){}
