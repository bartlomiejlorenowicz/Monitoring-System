package com.platform.kafka.consument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.dto.SecurityEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "security-events", groupId = "security-group")
    public void listen(String payload) throws JsonProcessingException {

        SecurityEventMessage event =
                objectMapper.readValue(payload, SecurityEventMessage.class);

        System.out.println("Received event: " + event);
    }
}
