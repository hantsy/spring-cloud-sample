package com.example.demo;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
public class PostServiceWireMockTest {

    @Autowired
    private PostServiceClient client;

    @Before
    public void setupWireMock() throws Exception {

        stubFor(
            get("/posts")
                .willReturn(
                    aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("[{ \"id\":\"1\", \"title\":\"My first post\", \"content\":\"Content of my first post\"},{ \"id\":\"2\", \"title\":\"My second post\", \"content\":\"Content of my second post\"}]")
                )
        );

        stubFor(
            get("/posts/1")
                .willReturn(
                    aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("{ \"id\":\"1\", \"title\":\"My first post\", \"content\":\"Content of my first post\"}")
                )
        );

    }

    @Test
    public void getAllPosts() {
        Flux<Post> posts = this.client.getAllPosts();
        StepVerifier
            .create(posts)
            .expectNext(new Post("1", "My first post", "Content of my first post"))
            .expectNext(new Post("2", "My second post", "Content of my second post"))
            .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<Post> post = this.client.findById("1");
        StepVerifier
            .create(post)
            .expectNext(new Post("1", "My first post", "Content of my first post"))
            .verifyComplete();
    }

}
