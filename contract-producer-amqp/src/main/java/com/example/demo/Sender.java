package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Notification notification){
        this.amqpTemplate.convertAndSend("notification.exchange", "notification.messages", notification);
    }

}
