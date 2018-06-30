package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
public class ContractConsumerStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContractConsumerStreamApplication.class, args);
	}

//	@Bean
//	public MessageConverter messageConverter(ObjectMapper objectMapper) {
//		final Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
//		jsonMessageConverter.setCreateMessageIds(true);
//		final ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter(jsonMessageConverter);
//		messageConverter.addDelegate(CONTENT_TYPE_JSON, jsonMessageConverter);
//		return messageConverter;
//	}
}
