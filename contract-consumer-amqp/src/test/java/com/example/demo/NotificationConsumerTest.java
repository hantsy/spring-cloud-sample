package com.example.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "stubrunner.amqp.enabled=true")
@AutoConfigureStubRunner(
    ids = "com.example:contract-producer-amqp:+:stubs:8090",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class NotificationConsumerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Captor
    ArgumentCaptor<Notification> argumentCaptor;

    @Autowired
    Receiver receiver;

    @Test
    public void testOnMessageReceived() {
        stubTrigger.trigger("notification.event");
        assertEquals("test message", this.receiver.getMessage());
    }

}
