package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {

    @GetMapping
    List<Notification> all() {
        return Arrays.asList(
            Notification.builder().body("rest notification 1").build(),
            Notification.builder().body("rest notification 2").build()
        );
    }

    @GetMapping("{id}")
    Notification get(@PathVariable String id) {
        if ("404".equals(id)) {
            throw new NotificaitonNotFoundException("notification:" + id + " not found");
        }
        return Notification.builder().body("test message").build();
    }
}
