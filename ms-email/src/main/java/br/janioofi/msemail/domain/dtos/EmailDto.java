package br.janioofi.msemail.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailDto implements Serializable {
    @Serial
    private static final long serialVersionUID= 1L;

    @NotBlank
    @Email
    private String emailTo;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
