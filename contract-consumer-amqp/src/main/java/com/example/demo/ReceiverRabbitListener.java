package com.example.demo;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Data
public class ReceiverRabbitListener {

    private String message;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = ContractConsumerAmqpApplication.queueName),
            exchange = @Exchange(
                value = ContractConsumerAmqpApplication.exchangeName,
                ignoreDeclarationExceptions = "true"
            )
        )
    )
    public void onNotificationReceived(Notification notification) {
        log.debug("payload::" + notification);
        this.message = notification.getBody();
    }

}
