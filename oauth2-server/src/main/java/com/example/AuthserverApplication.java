package com.example;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootApplication
public class AuthserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthserverApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

@SessionAttributes("authorizationRequest")
@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

}


@Configuration
@Order(-20)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
            .formLogin().loginPage("/login").permitAll()
        .and()
            .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
        .and()
            .authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}


@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .requestMatchers().antMatchers("/resources/**")
            .and()
            .authorizeRequests().antMatchers("/resources/userinfo").access("#oauth2.hasScope('profile')");
    }
}

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    AuthorizationServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
            new ClassPathResource("keystore.jks"), "foobar".toCharArray())
            .getKeyPair("test");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(this.jwtAccessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //@formatter:off
        clients
            .inMemory()
                .withClient("client")
                .secret("{noop}secret")
                .authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes("profile")
                .autoApprove(true)
                .redirectUris("http://localhost:8080/login", "http://localhost:8080/login/oauth2/code/login-client")
            .and()
                .withClient("trusted-app")
                .secret("{noop}secret")
                .authorizedGrantTypes("password")
                .autoApprove(true)
                .scopes("profile");
        //@formatter:on

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
        throws Exception {
        endpoints
            .tokenStore(this.tokenStore())
            .authenticationManager(authenticationManager)
            .accessTokenConverter(jwtAccessTokenConverter());

        endpoints.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(HttpServletRequest request,
                                   HttpServletResponse response, Object handler,
                                   ModelAndView modelAndView) throws Exception {
                if (modelAndView != null
                    && modelAndView.getView() instanceof RedirectView) {
                    RedirectView redirect = (RedirectView) modelAndView.getView();
                    String url = redirect.getUrl();
                    if (url.contains("code=") || url.contains("error=")) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
        throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }

}

@RestController
class UserinfoController {

    @GetMapping("/resources/userinfo")
    public Map<String, String> user(Principal user) {
        Map<String, String> userinfo = Collections.singletonMap("name", user.getName());
        return userinfo;
    }

}

@Service
@RequiredArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = User.withUsername("user")
            .password("password")
            .passwordEncoder(this.passwordEncoder::encode)
            .roles("USER")
            .build();

        UserDetails admin = User.withUsername("admin")
            .password("password")
            .passwordEncoder(this.passwordEncoder::encode)
            .roles("ADMIN")
            .build();

        return Stream.of(user, admin)
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("couldn't find the username " + username + "!"));
    }
}
