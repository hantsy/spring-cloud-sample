package com.example.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}


@Component
class HelloClient {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    String hello() {
        return this.restTemplate.getForObject("http://localhost:8080/hello", String.class);
    }

    String helloFallback() {
        return "Fallback: Hello world!";
    }
}

@RestController
@RequestMapping("/hello")
class HelloController {

    @GetMapping("")
    ResponseEntity<String> get() throws InterruptedException {
        Thread.sleep(new Random().nextInt(1500));
        return ok("Hello world");
    }
}

@RestController
@RequestMapping("/helloclient")
class HelloClientController {

    @Autowired
    HelloClient helloClient;

    @GetMapping("")
    String get() {
        return this.helloClient.hello();
    }
}

