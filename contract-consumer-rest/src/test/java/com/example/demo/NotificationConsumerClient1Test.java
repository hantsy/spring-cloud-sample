package com.example.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureStubRunner(
    ids = "com.example:contract-producer-rest-per-consumer:+:stubs:8090",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    consumerName = "client1",
    stubsPerConsumer = true
)
public class NotificationConsumerClient1Test {

    @Autowired
    NotificatinClient client;

    @Test
    public void testGetNotificationById() {
        Notification notification = this.client.getNotificationById("1");
        assertNotNull(notification);
    }

}
