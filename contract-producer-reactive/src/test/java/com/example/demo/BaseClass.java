package com.example.demo;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server.port=0")
public class BaseClass {

    @LocalServerPort
    private int port;

//    @Configuration
//    @Import(ProducerApplication.class)
//    public static class TestConfiguration {
//    }

    @MockBean
    private PostRepository posts;

    @Before
    public void before() throws Exception {

        RestAssured.baseURI = "http://localhost:" + this.port;

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
}