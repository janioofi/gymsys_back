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
    @JsonFormat(pattern = "dd/MM/yyyy") @Past LocalDate data_nascimento,
    @PastOrPresent @JsonFormat(pattern = "dd/MM/yyyy") LocalDate data_admissao,
    Long usuario
){}
