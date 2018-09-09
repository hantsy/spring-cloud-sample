package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
class PostController{

    private final PostRepository posts;

    @GetMapping()
    Flux<Post> getAll(){
        return this.posts.findAll();
    }

    @GetMapping("{id}")
    Mono<Post> findById(@PathVariable String id) {
        return this.posts.findById(id);
    }
}

interface PostRepository extends ReactiveMongoRepository<Post, String> {
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Post {
    @Id
    private String id;
    private String title;
    private String content;
}