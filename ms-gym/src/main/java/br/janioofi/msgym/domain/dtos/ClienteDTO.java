package br.janioofi.msgym.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteDTO {

    private Long id_cliente;
    private String nome;
    private String sobrenome;
    private String apelido;
    private String cpf;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
    private LocalDate data_nascimento;
    private Long plano;
}
