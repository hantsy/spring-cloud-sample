package com.example.demo;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
//@org.springframework.cloud.contract.wiremock.AutoConfigureWireMock(port = 8080)
@AutoConfigureStubRunner(ids = "com.example:contract-producer-reactive:+:8080",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class PostServiceClientTest {

    @Autowired
    private PostServiceClient client;

    //		@Before
//    public void setupWireMock() throws Exception {
//
//        WireMock.stubFor(
//            WireMock
//                .get("/customers")
//                .willReturn(
//                    WireMock
//                        .aResponse()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
//                        .withBody("[{ \"id\":\"1\", \"name\":\"Jane\"},{ \"id\":\"2\", \"name\":\"John\"}]")
//                )
//        );
//
//    }

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
