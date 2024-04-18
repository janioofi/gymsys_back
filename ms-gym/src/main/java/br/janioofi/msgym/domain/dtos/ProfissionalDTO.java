package br.janioofi.msgym.domain.dtos;

import br.janioofi.msgym.domain.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalDTO {
    private Long id_profissional;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
    private LocalDate data_nascimento;
    @PastOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_admissao;
    private Set<Usuario> usuarios = new HashSet<>();
}
