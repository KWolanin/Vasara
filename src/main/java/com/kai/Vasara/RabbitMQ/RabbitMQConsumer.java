package com.kai.Vasara.RabbitMQ;

import com.kai.Vasara.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class RabbitMQConsumer {

    private final EmailService emailService;

    @Autowired
    public RabbitMQConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email-notifications")
    public void receiveMessage(String message) {
        emailService.sendMailsToFollowers(message);
    }
}
