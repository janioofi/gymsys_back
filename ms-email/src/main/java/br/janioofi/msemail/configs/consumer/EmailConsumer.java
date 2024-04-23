package br.janioofi.msemail.configs.consumer;

import br.janioofi.msemail.domain.dtos.EmailDto;
import br.janioofi.msemail.domain.entities.Email;
import br.janioofi.msemail.domain.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final EmailService service;

    @RabbitListener(queues = "${mq.queues.ms-email}")
    public void receberSolicitacaoEmail(@Payload EmailDto emailDto){
        Email email = new Email();
        BeanUtils.copyProperties(emailDto, email);
        service.sendEmail(email);
        log.info("Enviando e-mail para " + emailDto.emailTo());
    }
}
