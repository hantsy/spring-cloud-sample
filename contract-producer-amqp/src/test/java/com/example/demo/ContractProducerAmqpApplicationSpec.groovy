package com.example.demo

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.toomuchcoding.jsonassert.JsonAssertion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.inject.Inject

// Context configuration would end up in base class
@ContextConfiguration(classes = [ContractProducerAmqpApplication], loader = SpringBootContextLoader)
@AutoConfigureMessageVerifier
@SpringBootTest(properties = "stubrunner.amqp.enabled=true")
class ContractProducerAmqpApplicationSpec extends Specification {

    // ALL CASES
    @Inject
    ContractVerifierMessaging contractVerifierMessaging

    @Inject
    ContractVerifierObjectMapper contractVerifierObjectMapper

    def "should work for triggered based messaging"() {
        //@formatter:off
        given:
            def dsl = Contract.make {
                description("""
                    send messages by rabbitmq
                """)
                label "notification.event"
                // input to the contract
                input {
                    // the contract will be triggered by a method
                    triggeredBy('send()')
                }
                outputMessage {
                    sentTo "notification.exchange"
                    body([
                        body: "test message",
                        type: "MESSAGE"
                    ])
                    headers {
                        header('contentType', 'application/json')
                        header('__TypeId__', 'com.example.demo.Notification')
                    }
                }
            }

        // generated test should look like this:
        when:
            send()
        then:
            def response = contractVerifierMessaging.receive('notification.exchange')
            response.headers.get('contentType') == 'application/json'
        and:
            DocumentContext parsedJson = JsonPath.parse(contractVerifierObjectMapper.writeValueAsString(response.payload))
            JsonAssertion.assertThat(parsedJson).field('type').isEqualTo('MESSAGE')
            JsonAssertion.assertThat(parsedJson).field('body').isEqualTo('test message')
        ////@formatter:on
    }

    @Autowired
    Sender sender

    void send() {
        this.sender.send(Notification.builder().body("test message").build())
    }
}