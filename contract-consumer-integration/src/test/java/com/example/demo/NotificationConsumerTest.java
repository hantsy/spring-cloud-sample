package com.example.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
//@SpringBootTest(properties = {"stubrunner.amqp.enabled=true", "stubrunner.stream.enabled=false", "stubrunner.integration.enabled=false"})
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.example:contract-producer-integration:+:stubs:8090",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class NotificationConsumerTest {

    @Autowired
    StubFinder stubFinder;

    @Autowired
    Receiver receiver;

    @Test
    public void testOnMessageReceived() {
        stubFinder.trigger("notification.event");
        assertEquals("test message", this.receiver.getMessage());
    }

}
