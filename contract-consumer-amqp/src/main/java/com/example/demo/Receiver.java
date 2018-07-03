package com.example.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver {
    public void handleMessage(Notification notification) {
        log.debug("received::<" + notification + ">");
    }
}
