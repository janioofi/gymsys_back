package br.janioofi.msgym.domain.dtos;

public record EmailDto(
    String emailTo,
    String subject,
    String text
){}
