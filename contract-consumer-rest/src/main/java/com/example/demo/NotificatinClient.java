package com.example.demo;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class NotificatinClient {

    private final RestTemplate restTemplate;

    Notification getNotificationById(String id) {
        try {
            ResponseEntity<Notification> response = this.restTemplate.getForEntity("http://localhost:8090/notifications/{id}", Notification.class, id);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == NOT_FOUND) {
                return null;
            }
        }
        return null;
    }

}
