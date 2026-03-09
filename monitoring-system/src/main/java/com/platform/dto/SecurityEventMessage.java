package com.platform.dto;

import com.platform.model.EventType;

public record SecurityEventMessage(
        EventType eventType,
        String userId,
        String ipAddress,
        String userAgent
) {}