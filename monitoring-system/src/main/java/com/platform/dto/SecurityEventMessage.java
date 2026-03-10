package com.platform.dto;

import com.platform.model.EventType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SecurityEventMessage(
        String eventId,
        String userId,
        String ipAddress,
        String userAgent,
        EventType eventType,
        Instant timestamp
) {}