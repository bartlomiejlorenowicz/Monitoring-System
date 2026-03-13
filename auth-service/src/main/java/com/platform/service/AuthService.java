package com.platform.service;

import com.platform.client.KeycloakClient;
import com.platform.dto.request.LoginRequest;
import com.platform.dto.request.RegistrationRequest;
import com.platform.dto.response.RegistrationResponse;
import com.platform.dto.response.TokenResponse;
import com.platform.events.EventType;
import com.platform.events.SecurityEventMessage;
import com.platform.model.User;
import com.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakUserService keycloakUserService;
    private final UserRepository userRepository;
    private final KeycloakClient keycloakClient;
    private final SecurityEventProducer eventProducer;

    public RegistrationResponse register(RegistrationRequest request) {

        String keycloakId = keycloakUserService.createUser(
                request.email(),
                request.password()
        );

        keycloakUserService.assignRole(keycloakId, "ROLE_USER");

        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setUsername(request.email());

        userRepository.save(user);

        SecurityEventMessage event =
                new SecurityEventMessage(
                        UUID.randomUUID().toString(),
                        request.email(),
                        null,
                        null,
                        EventType.USER_REGISTERED,
                        Instant.now()
                );

        eventProducer.send(event);

        return new RegistrationResponse(keycloakId, request.email());
    }

    public TokenResponse login(LoginRequest request) {

        try {

            TokenResponse token =
                    keycloakClient.login(request.username(), request.password());

            SecurityEventMessage successEvent =
                    new SecurityEventMessage(
                            UUID.randomUUID().toString(),
                            request.username(),
                            null,
                            null,
                            EventType.LOGIN_SUCCESS,
                            Instant.now()
                    );

            eventProducer.send(successEvent);

            return token;

        } catch (Exception e) {

            SecurityEventMessage failedEvent =
                    new SecurityEventMessage(
                            UUID.randomUUID().toString(),
                            request.username(),
                            null,
                            null,
                            EventType.LOGIN_FAILED,
                            Instant.now()
                    );

            eventProducer.send(failedEvent);

            throw new RuntimeException("Login failed");
        }
    }
}