package com.example.demo;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest
@RunWith(SpringRunner.class)
public class PostControllerTest {

    @Inject
    WebTestClient client;

    @MockBean
    private PostRepository posts;

    @Before
    public void before() throws Exception {

        when(this.posts.findAll())
            .thenReturn(
                Flux.just(
                    new Post("1", "My first post", "Content of my first post"),
                    new Post("2", "My second post", "Content of my second post")
                )
            );

        when(this.posts.findById(anyString()))
            .thenReturn(
                Mono.just(
                    new Post("1", "My first post", "Content of my first post")
                )
            );

    }

    @Test
    public void getAllPosts() {

        this.client
            .get()
            .uri("/posts")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo("1")
            .jsonPath("$.[0].title").isEqualTo("My first post")
            .jsonPath("$.[0].content").isEqualTo("Content of my first post")
            .jsonPath("$.[1].id").isEqualTo("2")
            .jsonPath("$.[1].title").isEqualTo("My second post")
            .jsonPath("$.[1].content").isEqualTo("Content of my second post")
        ;
    }

    @Test
    public void findPostById() {

        this.client
            .get()
            .uri("/posts/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo("1")
            .jsonPath("$.title").isEqualTo("My first post")
            .jsonPath("$.content").isEqualTo("Content of my first post")

        ;
    }
}
