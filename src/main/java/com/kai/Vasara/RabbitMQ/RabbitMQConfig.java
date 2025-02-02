package com.kai.Vasara.RabbitMQ;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.addresses}")
    private String rabbitMqUrl;

    @Getter
    @Value("${spring.rabbitmq.template.exchange:default_exchange}")
    private String exchange;

    @Getter
    @Value("${spring.rabbitmq.template.routing-key:default_routing_key}")
    private String routingKey;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqUrl);
        return factory;
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email-notifications", true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    public Binding binding(Queue emailQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(emailQueue).to(directExchange).with("email");
    }
}
