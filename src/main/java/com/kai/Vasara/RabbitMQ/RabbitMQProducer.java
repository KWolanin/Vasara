package com.kai.Vasara.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    public void sendMessage(EmailService.DataStructure email) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String emailJson = objectMapper.writeValueAsString(email);
            rabbitTemplate.convertAndSend("exchange", "email", emailJson);
        } catch (Exception e) {
           log.warn("Unable to send message to queue");
        }
    }

}
