package com.platform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.dto.SecurityEventMessage;
import com.platform.model.OutboxEvent;
import com.platform.model.SecurityEvent;
import com.platform.repository.OutboxRepository;
import com.platform.repository.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SecurityEventService {

    private final SecurityEventRepository repository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveEvent(SecurityEventMessage request) throws JsonProcessingException {
        SecurityEvent event = mapToEntity(request);

        repository.save(event);

        OutboxEvent outbox = OutboxEvent.builder()
                .eventType(request.eventType())
                .topic("security-events")
                .payload(objectMapper.writeValueAsString(request))
                .published(false)
                .createdAt(Instant.now())
                .build();

        outboxRepository.save(outbox);
    }

    public SecurityEvent mapToEntity(SecurityEventMessage request) {

        return SecurityEvent.builder()
                .eventType(request.eventType())
                .userId(request.userId())
                .ipAddress(request.ipAddress())
                .userAgent(request.userAgent())
                .timestamp(Instant.now())
                .build();
    }
}
