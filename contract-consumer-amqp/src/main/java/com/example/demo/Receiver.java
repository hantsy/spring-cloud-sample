package com.example.demo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class Receiver {

    private String message;

    @RabbitListener(bindings = {
        @QueueBinding(
            value = @Queue(name = "notification"),
            exchange = @Exchange(name = "notification.exchange", type = ExchangeTypes.TOPIC),
            key = "notification.messages"
        )
    })
    public void onNotificationReceived(Notification notification) {
        log.debug("payload::" + notification);
        this.message = notification.getBody();
    }
}
