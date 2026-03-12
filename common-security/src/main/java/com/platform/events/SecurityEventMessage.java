package com.platform.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SecurityEventMessage(
        String eventId,

        @NotBlank
        String userId,

        String ipAddress,

        String userAgent,

        @NotNull
        EventType eventType,

        Instant timestamp
) {}
