package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@SpringBootApplication
public class ContractConsumerAmqpApplication {
    public static final String queueName = "notification";
    public static final String exchangeName = "notification.exchange";
    public static final String routingKey = "notification.#";

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



    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}

@Configuration
@Profile("!listener")
class MessageListenerAdapterConfig {

    @Bean
    public MessageListenerAdapter messageListenerAdapter(Receiver messageSubscriber, MessageConverter messageConverter) {
        return new MessageListenerAdapter(messageSubscriber, messageConverter);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(new Queue(ContractConsumerAmqpApplication.queueName)).to(new DirectExchange(ContractConsumerAmqpApplication.exchangeName)).with(ContractConsumerAmqpApplication.routingKey);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ContractConsumerAmqpApplication.queueName);
        container.setMessageListener(listenerAdapter);

        return container;
    }


    @Bean
    public Receiver messageSubscriber() {
        return new Receiver();
    }
}

@Configuration
@EnableRabbit
@Profile("listener")
class RabbitListenerConfig {

    @Bean
    public ReceiverRabbitListener receiverRabbitListener() {
        return new ReceiverRabbitListener();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}

