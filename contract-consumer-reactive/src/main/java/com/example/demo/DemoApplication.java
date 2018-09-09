package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    WebClient client() {
        return WebClient.builder().build();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Post {
    private String id;
    private String title;
    private String content;
}


@Component
@RequiredArgsConstructor
class PostServiceClient {

    private final WebClient webClient;


    Flux<Post> getAllPosts() {
        return this.webClient
            .get()
            .uri("http://localhost:8080/posts")
            .retrieve()
            .bodyToFlux(Post.class);
    }

    Mono<Post> findById(String id) {
        return this.webClient
            .get()
            .uri("http://localhost:8080/posts/{id}", id)
            .retrieve()
            .bodyToMono(Post.class);
    }
}


