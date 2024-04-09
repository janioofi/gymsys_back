package br.janioofi.msgym.domain.DTOS;

import br.janioofi.msgym.domain.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProfissionalDTO {
    private Long id_profissional;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_nascimento;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_admissao;
    private List<Usuario> usuarios = new ArrayList<>();
}
