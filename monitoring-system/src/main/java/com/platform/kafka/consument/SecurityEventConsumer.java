package com.platform.kafka.consument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.dto.SecurityEventMessage;
import com.platform.model.ProcessedEvent;
import com.platform.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityEventConsumer {

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = "security-events", groupId = "audit-service")
    @Transactional
    public void listen(String payload) throws JsonProcessingException {

        SecurityEventMessage event =
                objectMapper.readValue(payload, SecurityEventMessage.class);

        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Skipping duplicate eventId={}", event.eventId());
            return;
        }

        log.info("Processing eventId={}, type={}", event.eventId(), event.eventType());

        processedEventRepository.save(
                ProcessedEvent.builder()
                        .eventId(event.eventId())
                        .processedAt(Instant.now())
                        .build()
        );

        System.out.println("Received event: " + event);
    }
}
