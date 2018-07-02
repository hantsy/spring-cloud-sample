package com.example.demo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(properties = {"stubrunner.amqp.enabled=true", "debug=true"})
@RunWith(SpringRunner.class)
@AutoConfigureMessageVerifier
public class MessageVerifierBase {

    @Autowired
    MessageVerifier verifier;

    @Autowired
    Sender sender;

    @MockBean
    RabbitTemplate rabbitTemplate;

    public void send() {
        this.sender.send(Notification.builder().body("test message").build());
    }

    @Before
    public void setup() {
        //verifier.receive("notification.exchange", 100, TimeUnit.MILLISECONDS);
    }
}
