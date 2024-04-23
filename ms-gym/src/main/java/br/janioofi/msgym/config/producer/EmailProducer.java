package br.janioofi.msgym.config.producer;

import br.janioofi.msemail.domain.dtos.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${mq.queues.ms-email}")
    private String routingKey;

    public void publishMessageEmail(EmailDto email){
        rabbitTemplate.convertAndSend("", routingKey, email);
    }

}
