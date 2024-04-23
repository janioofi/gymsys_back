package br.janioofi.msemail.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailDto {
    @NotBlank
    @Email
    private String emailTo;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
