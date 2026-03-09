package com.platform.dto;

import java.time.Instant;

public record SecurityEventRequest(
        String eventType,
        String userId,
        String ipAddress,
        Instant timestamp
) {}
