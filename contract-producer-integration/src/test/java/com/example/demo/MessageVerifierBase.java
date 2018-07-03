package com.example.demo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(properties = "stubrunner.amqp.enabled=true")
@RunWith(SpringRunner.class)
@AutoConfigureMessageVerifier
public class MessageVerifierBase {

    @Autowired
    MessageVerifier verifier;

    @Autowired
    ContractProducerIntegrationApplication.Sender sender;

    public void send() {
        this.sender.send(Notification.builder().body("test message").build());
    }

    @Before
    public void setup() {

        verifier.receive("output", 100, TimeUnit.MILLISECONDS
        );
    }
}
