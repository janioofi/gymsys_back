package br.janioofi.msemail.domain.dtos;

public record EmailDto(
    String emailTo,
    String subject,
    String text
){}
