package com.example.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

@DataMongoTest
@RunWith(SpringRunner.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private final Post one = new Post("1", "My first post", "Content of my first post");
    private final Post two = new Post("2", "My second post", "Content of my second post");

    @Test
    public void findAllPosts() throws Exception {

        Publisher<Post> setup = this.postRepository
            .deleteAll()
            .thenMany(this.postRepository.saveAll(Flux.just(this.one, this.two)));

        Publisher<Post> all = this.postRepository.findAll();

        Publisher<Post> composite = Flux
            .from(setup)
            .thenMany(all);

        StepVerifier
            .create(composite)
            .expectNextSequence(Arrays.asList(this.one, this.two))
            .verifyComplete();

    }
}
