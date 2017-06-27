package com.hantsylabs.sample.springmicroservice.auth;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

/**
 * Created by hantsy on 6/17/2017.
 */
@Configuration
@Order(-20)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .httpBasic()
            .and()
                .formLogin().loginPage("/login").permitAll()
            .and()
                .authorizeRequests().antMatchers("/login", "/signup", "/oauth/authorize", "/oauth/confirm_access").permitAll()
            .and()
                .authorizeRequests().anyRequest().authenticated();
//            .and()
//                .csrf().disable();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager);
    }
}
