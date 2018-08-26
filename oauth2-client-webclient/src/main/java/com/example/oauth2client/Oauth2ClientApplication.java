package com.example.oauth2client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
public class Oauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ClientApplication.class, args);
	}
}

@RestController
class ProfileRestController {

	private final OAuth2AuthorizedClientService clientService;

	ProfileRestController(OAuth2AuthorizedClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping("/")
	ResponseEntity profile(OAuth2AuthenticationToken token) {
		OAuth2AuthorizedClient authorizedClient = this.clientService
			.loadAuthorizedClient(
				token.getAuthorizedClientRegistrationId(),
				token.getName());

		String userInfoEndpointUri = authorizedClient
			.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUri();

		if (!StringUtils.isEmpty(userInfoEndpointUri)) {	// userInfoEndpointUri is optional for OIDC Clients
            PrincipalDetails userAttributes = WebClient.builder()
				.filter(oauth2Credentials(authorizedClient))
				.build()
				.get()
				.uri(userInfoEndpointUri)
				.retrieve()
				.bodyToMono(PrincipalDetails.class)
				.block();

			return ok(userAttributes);
		}

		return badRequest().build();
	}

	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
		return this.clientService.loadAuthorizedClient(
			authentication.getAuthorizedClientRegistrationId(), authentication.getName());
	}

	private ExchangeFilterFunction oauth2Credentials(OAuth2AuthorizedClient authorizedClient) {
		return ExchangeFilterFunction.ofRequestProcessor(
			clientRequest -> {
				ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue())
					.build();
				return Mono.just(authorizedRequest);
			});
	}
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class PrincipalDetails {
    private String name;
}

