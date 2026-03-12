package com.platform.events;

import java.time.Instant;

public record AuthEvent(

        String eventId,
        String username,
        EventType eventType,
        Instant timestamp

) {}
