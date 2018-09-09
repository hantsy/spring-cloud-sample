package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class ProviderPactApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderPactApplication.class, args);
    }


    @Bean
    CommandLineRunner init(NotificaitonRepository notificaitonRepository) {

        return args -> {

            Arrays.asList(
                Notification.builder().body("rest notification 1").build(),
                Notification.builder().body("rest notification 2").build()
            ).forEach(
                notification -> notificaitonRepository.save(notification)
            );

            notificaitonRepository.findAll()
                .forEach(
                    notification -> log.debug("Notification::" + notification)
                );
        };

    }
}




