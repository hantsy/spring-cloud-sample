package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@SpringBootApplication
@EnableBinding(Source.class)
public class ContractProducerStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractProducerStreamApplication.class, args);
    }

}


