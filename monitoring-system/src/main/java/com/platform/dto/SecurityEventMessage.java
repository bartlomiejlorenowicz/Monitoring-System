package com.platform.dto;

import com.platform.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
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