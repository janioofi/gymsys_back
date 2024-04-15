package br.janioofi.msgym.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Sobrenome é obrigatório")
    private String sobrenome;

    private String apelido;

    @CPF
    @NotNull(message = "CPF é obrigatório")
    private String cpf;

    @Email
    @NotNull(message = "E-mail é obrigatório")
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_nascimento;

    @OneToOne
    @NotNull(message = "Plano é obrigatório")
    private Plano plano;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_cadastro = LocalDate.now();

    @JsonFormat(pattern = "dd/MM/yyyy HH:ss")
    private LocalDateTime data_atualizacao;
}
