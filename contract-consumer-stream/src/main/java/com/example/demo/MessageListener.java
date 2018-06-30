package com.example.demo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class MessageListener {

    private String message;

    @StreamListener(Processor.INPUT)
    public void onNotificationReceived(Message notification) {
        log.debug("payload::" + notification);
        this.message = notification.getBody();
    }
}
