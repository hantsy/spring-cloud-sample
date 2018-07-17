package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class UiApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }

    @RequestMapping("/")
    public String home(Principal user) {
        return "Hello " + user.getName();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        //@formatter:off
        http.requestMatchers().antMatchers("/**")
            .and()
            .authorizeRequests().anyRequest().authenticated();
        //@formatter:on
    }

}
