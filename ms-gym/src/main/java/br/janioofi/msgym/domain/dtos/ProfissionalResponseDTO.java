package br.janioofi.msgym.domain.dtos;

import java.time.LocalDate;

public record ProfissionalResponseDTO(
    Long id_profissional,
    String nome,
    String sobrenome,
    String cpf,
    String email,
    LocalDate data_nascimento,
    LocalDate data_admissao,
    String usuario
){}
