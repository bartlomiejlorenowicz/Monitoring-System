package com.platform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.dto.SecurityEventMessage;
import com.platform.model.OutboxEvent;
import com.platform.model.OutboxStatus;
import com.platform.model.SecurityEvent;
import com.platform.repository.OutboxRepository;
import com.platform.repository.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityEventService {

    private final SecurityEventRepository securityEventRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveEvent(SecurityEventMessage request) throws JsonProcessingException {
        String eventId = request.eventId() != null ? request.eventId() : UUID.randomUUID().toString();
        Instant now = Instant.now();

        SecurityEvent securityEvent  = SecurityEvent.builder()
                .eventType(request.eventType())
                .userId(request.userId())
                .ipAddress(request.ipAddress())
                .userAgent(request.userAgent())
                .timestamp(now)
                .build();

        securityEventRepository.save(securityEvent);

        SecurityEventMessage eventToPublish= new SecurityEventMessage(
                eventId,
                request.userId(),
                request.ipAddress(),
                request.userAgent(),
                request.eventType(),
                now
        );

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventId(eventId)
                .eventType(request.eventType())
                .topic("security-events")
                .payload(objectMapper.writeValueAsString(eventToPublish))
                .status(OutboxStatus.NEW)
                .createdAt(now)
                .retryCount(0)
                .build();

        outboxRepository.save(outboxEvent);
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
