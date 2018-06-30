package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final Source source;

    public void send(Message notification) {
        this.source.output().send(MessageBuilder.withPayload(notification).build());
    }

}
