package com.platform.client;

import com.platform.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final WebClient keycloakWebClient;

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public TokenResponse login(String username, String password) {

        String tokenEndpoint =
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        log.info("Requesting token from Keycloak for user={}", username);

        return keycloakWebClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(
                        "grant_type=password" +
                                "&client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&username=" + username +
                                "&password=" + password
                )
                .retrieve()
                .bodyToMono(TokenResponse.class)

                .timeout(Duration.ofSeconds(5))

                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(200))
                                .filter(ex -> {
                                    log.warn("Retrying Keycloak request due to {}", ex.getMessage());
                                    return true;
                                })
                )

                .doOnError(ex ->
                        log.error("Keycloak login failed for user={}", username)
                )

                .block();
    }
}