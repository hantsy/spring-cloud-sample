package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@SpringBootApplication
public class ContractProducerAmqpApplication {

    private final String queueName = "notification";
    private final String exchangeName = "notification.exchange";
    private final String routingKey = "notification.#";

    public static void main(String[] args) {
        SpringApplication.run(ContractProducerAmqpApplication.class, args);
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
        jsonMessageConverter.setCreateMessageIds(true);
        final ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter(jsonMessageConverter);
        messageConverter.addDelegate(CONTENT_TYPE_JSON, jsonMessageConverter);
        return messageConverter;
    }
}


