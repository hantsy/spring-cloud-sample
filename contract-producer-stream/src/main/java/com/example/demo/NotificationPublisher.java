package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final Source source;

    public void send(Notification notification) {
        this.source.output().send(MessageBuilder.withPayload(notification).build());
    }

}
