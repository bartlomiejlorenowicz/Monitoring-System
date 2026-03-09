package com.platform.kafka.consument;

import com.platform.model.SecurityEvent;
import com.platform.service.SecurityEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityEventConsumer {

    private final SecurityEventService service;

    @KafkaListener(topics = "security-events", groupId = "security-group")
    public void listen(SecurityEvent event) {

        service.saveEvent(event);

        System.out.println("Received event: " + event);
    }
}
