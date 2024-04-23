package br.janioofi.msemail.domain.services;

import br.janioofi.msemail.domain.entities.Email;
import br.janioofi.msemail.domain.enums.StatusEmail;
import br.janioofi.msemail.domain.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository repository;
    private final JavaMailSender emailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public Email sendEmail(Email email){
        try {
            email.setSendDateEmail(LocalDateTime.now());
            email.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            emailSender.send(message);
            email.setStatusEmail(StatusEmail.ENVIADO);
        }catch (MailException e){
            email.setStatusEmail(StatusEmail.ERROR);
        }
        repository.save(email);
        return email;
    }
}