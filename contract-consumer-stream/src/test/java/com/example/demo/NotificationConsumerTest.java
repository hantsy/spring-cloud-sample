package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.messaging.Message;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "debug=true")
@AutoConfigureStubRunner(
    ids = "com.example:contract-producer-stream:+:stubs:8090",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@Slf4j
public class NotificationConsumerTest {

    @Autowired
    StubFinder stubFinder;

    @Autowired
    MessageVerifier<Message<?>> messageVerifier;


    @Test
    public void testOnMessageReceived() {
        stubFinder.trigger("notification.event");
        Message<?> message = messageVerifier.receive("notification");
        log.debug("payload::" + message);
        assertNotNull(message.getPayload());
        //assertEquals("test message", this.receiver.getMessage());
    }

}
