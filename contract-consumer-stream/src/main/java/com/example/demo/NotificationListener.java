package com.example.demo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class NotificationListener {

    private String message;

    @StreamListener(Sink.INPUT)
    public void onNotificationReceived(Notification notification) {
        log.debug("payload::" + notification);
        this.message = notification.getBody();
    }
}
