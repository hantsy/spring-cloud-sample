package com.example.demo;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.reset;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("provider-pact")
//@PactBroker(
//    protocol = "https",
//    host = "${pactBrokerHost}",
//    port = "8443",
//    authentication = @PactBrokerAuth(
//        username = "${pactBrokerUser}",
//        password = "${pactBrokerPassword}"
//    )
//)
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProviderPactApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private NotificaitonRepository notificaitonRepository;

	@TestTarget
	public final Target target = new HttpTarget(8090);

	@Before
	public void before() {
		when(this.notificaitonRepository.findAll())
			.thenReturn(  Arrays.asList(
                Notification.builder().id(1L).body("rest notification 1").build(),
                Notification.builder().id(2L).body("rest notification 2").build()
            ));

		when(this.notificaitonRepository.findById(1L))
			.thenReturn(
				Optional.of(Notification.builder().id(1L).body("test message").build())
			);
        when(this.notificaitonRepository.findById(404L)).thenReturn(Optional.empty());

		reset();
		webAppContextSetup(this.webApplicationContext);
	}

	@State("should return notification if existed")
	public void shouldReturnUserIfExisted() {
		//@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/notifications/1")
        .then()
            //.body("id", is(1L))
            .body("body", is("test message"))
            .body("type", is("MESSAGE"))
            .statusCode(HttpStatus.SC_OK);
        //@formatter:on
	}

	@State("should return error 404 if not existed")
	public void shouldReturnError404IfExisted() {
		//@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/notifications/404")
        .then()
            .body("code", is("not_found"))
            .body("message", containsString("not found"))
            .statusCode(HttpStatus.SC_NOT_FOUND);
        //@formatter:on
	}

}
