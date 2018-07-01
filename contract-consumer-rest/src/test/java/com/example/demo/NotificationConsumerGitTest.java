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
    ids = "com.example:contract-producer-rest-git:0.0.1-SNAPSHOT:stubs:8090",
    stubsMode = StubRunnerProperties.StubsMode.REMOTE,
    repositoryRoot = "git://https://github.com/hantsy/contracts-git.git"
)
public class NotificationConsumerGitTest {

    @Autowired
    NotificatinClient client;

    @Test
    public void testGetNotificationById(){
        Notification notification =  this.client.getNotificationById("1");
        assertNotNull(notification);

        Notification notExisted =  this.client.getNotificationById("404");
        assertNull(notExisted);
    }

}
