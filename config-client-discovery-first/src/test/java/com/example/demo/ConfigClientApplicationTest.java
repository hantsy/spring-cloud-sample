package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigClientApplicationTest {

	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	private MessageRestController controller;

	@Autowired
	private ContextRefresher refresher;

	@Test
	public void contextLoads() {
		assertThat(controller.getMessage()).isNotEqualTo("Hello test");
		TestPropertyValues
			.of("message:Hello test")
			.applyTo(environment);
		assertThat(controller.getMessage()).isNotEqualTo("Hello test");
		refresher.refresh();
		assertThat(controller.getMessage()).isEqualTo("Hello test");
	}

}