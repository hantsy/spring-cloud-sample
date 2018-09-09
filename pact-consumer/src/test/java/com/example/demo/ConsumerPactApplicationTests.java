package com.example.demo;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerPactApplicationTests {

    @Autowired
    NotificatinClient client;


    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("provider-pact", "localhost", 8090, this);

    @Before
    public void setup() {

    }

    @Pact(consumer = "consumer-pact")
    public RequestResponsePact createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        //@formatter:off
        return builder
            .given("should return notification if existed")
                .uponReceiving("Get a notification by existing id: 1")
                .path("/notifications/1")
                .method("GET")
            .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{\"body\": \"test message\", \"type\": \"MESSAGE\" }")
            .given("should return error 404 if not existed")
                .uponReceiving("Get a notification by none-existing id: 404")
                .method("GET")
                .path("/notifications/404")
            .willRespondWith()
                .headers(headers)
                .status(404)
                .body("{\"code\": \"not_found\",\"message\": \"Notification: 404 was not found\"}")
            .toPact();
        //@formatter:on
    }


    @Test
    @PactVerification()
    public void testGetNotificationById() {
        Notification notification = this.client.getNotificationById("1");
        assertNotNull(notification);

        Notification notExisted = this.client.getNotificationById("404");
        assertNull(notExisted);
    }


}
