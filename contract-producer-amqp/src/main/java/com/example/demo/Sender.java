package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    RabbitTemplate amqpTemplate;

    public void send(Notification notification){
        this.amqpTemplate.convertAndSend("notification.exchange", "notification.messages", notification);
    }

}
