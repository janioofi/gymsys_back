package br.janioofi.msemail.controllers;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msemail.domain.entities.Email;
import br.janioofi.msemail.domain.services.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Email", description = "API Email")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/sending-email")
    public ResponseEntity<Email> sendingEmail(@RequestBody @Valid EmailDto emailDto) {
        Email emailModel = new Email();
        BeanUtils.copyProperties(emailDto, emailModel);
        service.sendEmail(emailModel);
        return new ResponseEntity<>(emailModel, HttpStatus.CREATED);
    }
}
