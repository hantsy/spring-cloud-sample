package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EurekaClientApplicationTests {

    @Autowired
    private EurekaDiscoveryClient client;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(this.client);
    }


    @Test
    @Ignore
    public void testGetMessageFromServiceInstance() {
        ResponseEntity<String> responseMessage = this.restTemplate
            .getForEntity(
                this.client.getInstances("eureka-client").get(0).getUri(),
                String.class
            );

        assertThat(responseMessage.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseMessage.getBody()).contains("greeting");
    }

}