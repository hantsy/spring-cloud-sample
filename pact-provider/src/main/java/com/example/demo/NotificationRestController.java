package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationRestController {
    private final NotificaitonRepository notificaitonRepository;

    @GetMapping
    List<Notification> all() {
        return this.notificaitonRepository.findAll();
    }

    @GetMapping("{id}")
    Notification get(@PathVariable Long id) {
        return this.notificaitonRepository.findById(id).orElseThrow(
            () -> new NotificationNotFoundException("Notification: " + id + " was not found")
        );
    }
}
