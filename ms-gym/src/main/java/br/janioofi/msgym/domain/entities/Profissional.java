package br.janioofi.msgym.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profissional implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_profissional;

    @NotNull(message = "Nome é obrigatório")
    @NotEmpty(message = "Campo nome não pode  estar vazio")
    private String nome;

    @NotNull(message = "Sobrenome é obrigatório")
    @NotEmpty(message = "Campo sobrenome não pode  estar vazio")
    private String sobrenome;

    @CPF
    @Column(unique = true)
    @NotNull(message = "CPF é obrigatório")
    @NotEmpty(message = "CPF nome não pode  estar vazio")
    private String cpf;

    @Email
    @Column(unique = true)
    @NotNull(message = "Email é obrigatório")
    @NotEmpty(message = "Campo email não pode  estar vazio")
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
    private LocalDate data_nascimento;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent
    private LocalDate data_admissao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Usuario> usuarios = new HashSet<>();

}
