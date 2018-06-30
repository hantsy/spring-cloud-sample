package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

@SpringBootApplication
@Slf4j
public class ContractProducerIntegrationApplication {

    private final String queueName = "notification";
    private final String exchangeName = "notification.exchange";
    private final String routingKey = "notification.#";

    public static void main(String[] args) {
        SpringApplication.run(ContractProducerIntegrationApplication.class, args);
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

    @Bean
    public IntegrationFlow amqpOutbound(AmqpTemplate amqpTemplate) {
        return IntegrationFlows.from(output())
            .handle(Amqp.outboundAdapter(amqpTemplate)
                .exchangeName(exchangeName)
                .routingKey("notification.messages")) // default exchange - route to queue 'foo'
            .get();
    }

    @Bean
    public MessageChannel output() {
        return new DirectChannel();
    }

    @MessagingGateway(defaultRequestChannel = "output")
    public interface Sender {
        void send(Notification data);
    }


}


