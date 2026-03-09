package com.platform.kafka.producer;

import com.platform.dto.SecurityEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityEventProducer {

    private final KafkaTemplate<String, SecurityEventMessage> kafkaTemplate;

    public void sendEvent(SecurityEventMessage event) {
        kafkaTemplate.send("security-events", event);
    }
}
