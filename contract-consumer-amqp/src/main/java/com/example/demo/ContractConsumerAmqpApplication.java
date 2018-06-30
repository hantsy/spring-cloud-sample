package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@SpringBootApplication
public class ContractConsumerAmqpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractConsumerAmqpApplication.class, args);
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
