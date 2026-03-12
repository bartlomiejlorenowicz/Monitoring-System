package com.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.events.SecurityEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(SecurityEventMessage event) {

        try {

            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "security-events",
                    event.eventId(),
                    payload
            );

            log.info("Security event sent eventId={}", event.eventId());

        } catch (Exception e) {

            log.error("Failed to send security event", e);
        }
    }
}
